package com.m4ykey.ui.album

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.lyrics.ui.LyricsActivity
import com.m4ykey.album.ui.R
import com.m4ykey.album.ui.databinding.FragmentAlbumDetailBinding
import com.m4ykey.core.network.UiState
import com.m4ykey.core.network.isInternetAvailable
import com.m4ykey.core.observeUiState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.buttonAnimation
import com.m4ykey.core.views.buttonsIntents
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.utils.copyText
import com.m4ykey.core.views.utils.formatAirDate
import com.m4ykey.core.views.utils.getColorFromImage
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.CopyrightEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.album.adapter.TrackAdapter
import com.m4ykey.ui.album.adapter.TrackEntityAdapter
import com.m4ykey.ui.album.helpers.animateColorTransition
import com.m4ykey.ui.album.helpers.getArtistList
import com.m4ykey.ui.album.helpers.getLargestImageUrl
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import com.m4ykey.ui.album.viewmodel.ColorViewModel
import com.m4ykey.ui.album.viewmodel.TrackViewModel
import com.m4ykey.ui.artist.ArtistViewModel
import com.m4ykey.ui.artist.adapter.ArtistAdapter
import com.m4ykey.ui.navigation.AlbumNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AlbumDetailFragment : BaseFragment<FragmentAlbumDetailBinding>(
    FragmentAlbumDetailBinding::inflate
) {

    private val args by navArgs<AlbumDetailFragmentArgs>()
    private val albumViewModel by viewModels<AlbumViewModel>()
    private val colorViewModel by viewModels<ColorViewModel>()
    private val trackViewModel by viewModels<TrackViewModel>()
    private val artistViewModel by viewModels<ArtistViewModel>()
    private val trackAdapter by lazy { createTrackAdapter() }
    private val trackEntityAdapter by lazy { createTrackEntityAdapter() }
    private val artistAdapter by lazy { createArtistAdapter() }
    @Inject
    lateinit var navigator : AlbumNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            toolbar.menu.findItem(R.id.imgStar)?.setOnMenuItemClickListener {
                val bottomSheet = AlbumStarBottomSheetFragment().apply {
                    arguments = Bundle().apply {
                        putString("albumId", args.albumId)
                    }
                }
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
                true
            }
        }

        lifecycleScope.launch {
            observeViewModel()
        }
    }

    private fun createTrackEntityAdapter() : TrackEntityAdapter {
        return TrackEntityAdapter()
    }

    private fun createArtistAdapter() : ArtistAdapter {
        return ArtistAdapter(
            onArtistClick = { item ->
                startActivity(Intent(Intent.ACTION_VIEW, item.externalUrls.spotify.toUri()))
            }
        )
    }

    private fun createTrackAdapter(): TrackAdapter {
        return TrackAdapter(
            onTrackClick = { track ->
                val trackName = track.name
                val artistName = track.getArtistList()
                val trackId = track.id

                val intent = Intent(requireContext(), LyricsActivity::class.java).apply {
                    putExtra("trackName", trackName)
                    putExtra("artistName", artistName)
                    putExtra("trackId", trackId)
                }
                startActivity(intent)
            }
        )
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            if (isInternetAvailable(requireContext())) {
                albumViewModel.getAlbumById(args.albumId)
                trackViewModel.getAlbumTracks(args.albumId)

                observeUiState(
                    flow = albumViewModel.detail,
                    progressBar = binding.progressbar,
                    context = requireContext(),
                    onSuccess = { detail ->
                        detail?.let { displayAlbumDetail(it) }
                        binding.nestedScrollView.isVisible = true
                    },
                    lifecycleScope = lifecycleScope
                )
            } else {
                try {
                    binding.progressbar.isVisible = true

                    val album = albumViewModel.getAlbum(args.albumId)

                    if (album != null) {
                        displayAlbumFromDatabase(album)
                        binding.progressbar.isVisible = false
                        binding.nestedScrollView.isVisible = true
                    } else {
                        binding.progressbar.isVisible = false
                        showToast(requireContext(), "Album not found in the database")
                    }
                } catch (e : Exception) {
                    binding.progressbar.isVisible = false
                    showToast(requireContext(), e.message ?: "Error loading album from database")
                }
            }
        }

        observeUiState(
            flow = trackViewModel.tracks,
            context = requireContext(),
            progressBar = binding.progressBarTracks,
            lifecycleScope = lifecycleScope,
            onSuccess = { tracks ->
                trackAdapter.submitList(tracks)
                binding.progressBarTracks.isVisible = false
            }
        )
    }

    private fun shareUrl(url : String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        val chooser = Intent.createChooser(shareIntent, url)
        context?.startActivity(chooser)
    }

    private fun displayAlbumFromDatabase(album: AlbumEntity) {
        binding.apply {
            with(album) {
                lifecycleScope.launch {
                    val albumWithStates = albumViewModel.getAlbumWithStates(id)
                    if (albumWithStates != null) {
                        updateIcons(albumWithStates)
                    }

                    val tracks = trackViewModel.getTracksById(id)
                    trackEntityAdapter.differ.submitList(tracks)

                    toolbar.menu.findItem(R.id.imgShare)?.setOnMenuItemClickListener {
                        shareUrl(albumUrl)
                        true
                    }

                    imgSave.setOnClickListener {
                        try {
                            lifecycleScope.launch {
                                val isAlbumSaved = albumViewModel.getSavedAlbumState(id)
                                val isListenLaterSaved = albumViewModel.getListenLaterState(id)
                                if (isListenLaterSaved?.isListenLaterSaved == true) {
                                    albumViewModel.deleteListenLaterState(id)
                                    if (isAlbumSaved == null) {
                                        albumViewModel.deleteAlbum(id)
                                        trackViewModel.deleteTracksById(id)
                                    }
                                    imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                                }
                                if (isAlbumSaved != null) {
                                    albumViewModel.deleteSavedAlbumState(id)
                                    if (isListenLaterSaved == null) {
                                        albumViewModel.deleteAlbum(id)
                                        trackViewModel.deleteTracksById(id)
                                    }
                                    imgSave.buttonAnimation(R.drawable.ic_favorite_border)
                                } else {
                                    albumViewModel.insertAlbum(album)
                                    albumViewModel.insertSavedAlbum(IsAlbumSaved(id, true))
                                    trackViewModel.insertTracks(tracks)
                                    imgSave.buttonAnimation(R.drawable.ic_favorite)
                                }
                                val updatedList = trackViewModel.getTracksById(id)
                                trackEntityAdapter.differ.submitList(updatedList)
                            }
                        } catch (e : Exception) {
                            showToast(requireContext(), "${getString(R.string.error_with_saving_album)} ${e.message}")
                        }
                    }

                    imgListenLater.setOnClickListener {
                        try {
                            lifecycleScope.launch {
                                val isListenLaterSaved = albumViewModel.getListenLaterState(id)
                                if (isListenLaterSaved != null) {
                                    albumViewModel.deleteListenLaterState(id)
                                    val isAlbumSaved = albumViewModel.getSavedAlbumState(id)
                                    if (isAlbumSaved == null) {
                                        albumViewModel.deleteAlbum(id)
                                        trackViewModel.deleteTracksById(id)
                                    }
                                    imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                                } else {
                                    albumViewModel.insertAlbum(album)
                                    albumViewModel.insertListenLaterAlbum(IsListenLaterSaved(id, true))
                                    trackViewModel.insertTracks(tracks)
                                    imgListenLater.buttonAnimation(R.drawable.ic_listen_later)
                                }
                                val updatedList = trackViewModel.getTracksById(id)
                                trackEntityAdapter.differ.submitList(updatedList)
                            }
                        } catch (e : Exception) {
                            showToast(requireContext(), "${getString(R.string.error_with_saving_album)} ${e.message}")
                        }
                    }
                }

                binding.rvTrackList.apply {
                    adapter = trackEntityAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }

                val copyrights = album.copyrights
                    .map { it.text }
                    .distinct()
                    .joinToString(separator = "\n")

                loadImage(imgAlbum, images)
                if (colorViewModel.selectedColor.value == null) {
                    getColorFromImage(
                        imageUrl = images,
                        imageView = imgAlbum,
                        onColorReady = { color ->
                            colorViewModel.setColor(color)
                            animateColorTransition("#4FC3F7".toColorInt(), color, btnAlbum, btnArtist)

                        }
                    )
                } else {
                    val color = colorViewModel.selectedColor.value ?: return
                    btnAlbum.setBackgroundColor(color)
                    btnArtist.setBackgroundColor(color)
                }

                cardView.setOnClickListener {
                    navigator.navigateToAlbumCover(images)
                }

                txtCopyrights.text = copyrights

                buttonsIntents(button = btnArtist, url = artists[0].url, requireContext())
                buttonsIntents(button = btnAlbum, url = albumUrl, requireContext())

                txtAlbumName.apply {
                    text = name
                    setOnClickListener { copyText(name, requireContext(), getString(com.m4ykey.core.R.string.copied_to_clipboard)) }
                }
                txtArtist.text = getArtistList()
                txtInfo.text = getString(
                    R.string.album_info,
                    albumType,
                    releaseDate,
                    totalTracks,
                    getString(R.string.tracks)
                )

                progressBarTracks.isVisible = false
                progressbar.isVisible = false
            }
        }
    }

    private suspend fun displayTrackDuration() {
        trackViewModel.totalTracksDuration.collect { totalDuration ->
            val hours = (totalDuration / 1000) / 3600
            val minutes = ((totalDuration / 1000) % 3600) / 60
            val seconds = (totalDuration / 1000) % 60

            val formattedDuration = when {
                hours > 0 -> String.format(Locale.getDefault(), "%d ${getString(R.string.hour)} %d min", hours, minutes)
                minutes > 0 -> String.format(Locale.getDefault(), "%d min %d ${getString(R.string.sec)}", minutes, seconds)
                else -> String.format(Locale.getDefault(), "%d ${getString(R.string.sec)}", seconds)
            }
            binding.txtTotalDuration.text = getString(R.string.duration, formattedDuration)
        }
    }

    private fun setupRecyclerView() {
        binding.rvTrackList.apply {
            adapter = trackAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        if (!trackViewModel.isPaginationEnded && trackViewModel.tracks.value !is UiState.Loading) {
                            lifecycleScope.launch { trackViewModel.getAlbumTracks(args.albumId) }
                        }
                    }
                }
            })

            lifecycleScope.launch {
                delay(500L)
                displayTrackDuration()
            }
        }
    }

    private fun displayAlbumDetail(item: AlbumDetail) {
        binding.apply {
            val albumType = when {
                item.totalTracks in 2..6 && item.albumType.equals("Single", ignoreCase = true) -> "EP"
                else -> item.albumType.replaceFirstChar { it.uppercase() }
            }
            val formatAirDate = formatAirDate(item.releaseDate)
            val albumInfo = "$albumType • $formatAirDate • ${item.totalTracks} " + getString(
                R.string.tracks
            )
            val albumUrl = item.externalUrls.spotify
            val artistUrl = item.artists[0].externalUrls.spotify
            val copyrights = item.copyrights
                .map { it.text }
                .distinct()
                .joinToString(separator = "\n")

            toolbar.menu.findItem(R.id.imgShare)?.setOnMenuItemClickListener {
                shareUrl(albumUrl)
                true
            }

            txtAlbumName.apply {
                text = item.name
                setOnClickListener { copyText(item.name, requireContext(), getString(com.m4ykey.core.R.string.copied_to_clipboard)) }
            }
            txtArtist.text = item.getArtistList()
            txtInfo.text = albumInfo

            cardView.setOnClickListener {
                navigator.navigateToAlbumCover(item.getLargestImageUrl().toString())
            }

            txtCopyrights.text = copyrights

            loadImage(imgAlbum, item.getLargestImageUrl().toString())

            if (colorViewModel.selectedColor.value == null) {
                getColorFromImage(
                    imageUrl = item.getLargestImageUrl().toString(),
                    imageView = imgAlbum,
                    onColorReady = { color ->
                        colorViewModel.setColor(color)
                        animateColorTransition("#4FC3F7".toColorInt(), color, btnAlbum, btnArtist)

                    }
                )
            } else {
                val color = colorViewModel.selectedColor.value ?: return
                btnAlbum.setBackgroundColor(color)
                btnArtist.setBackgroundColor(color)
            }

            buttonsIntents(button = btnAlbum, url = albumUrl, requireContext())
            btnArtist.setOnClickListener {
                if (item.artists.size > 1) {
                    val artistId = item.artists.joinToString(",") { it.id }
                    showDialog(artistId)
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW,
                        item.artists[0].externalUrls.spotify.toUri()))
                }
            }

            val artistsEntity = item.artists.map { artist ->
                ArtistEntity(
                    name = artist.name,
                    artistId = artist.id,
                    url = artistUrl,
                    albumId = args.albumId
                )
            }

            val copyrightEntity = item.copyrights.map { copyright ->
                CopyrightEntity(text = copyright.text)
            }

            val album = AlbumEntity(
                id = item.id,
                name = item.name,
                releaseDate = formatAirDate.toString(),
                totalTracks = item.totalTracks,
                images = item.getLargestImageUrl().toString(),
                albumType = albumType,
                artists = artistsEntity,
                albumUrl = albumUrl,
                saveTime = System.currentTimeMillis(),
                copyrights = copyrightEntity
            )

            val trackEntity = trackAdapter.differ.currentList.map { track ->
                TrackEntity(
                    albumId = args.albumId,
                    explicit = track.explicit,
                    id = track.id,
                    name = track.name,
                    durationMs = track.durationMs,
                    artists = track.artists.joinToString(", ") { it.name }
                )
            }

            lifecycleScope.launch {
                val albumWithStates = albumViewModel.getAlbumWithStates(item.id)
                if (albumWithStates != null) {
                    updateIcons(albumWithStates)
                }
            }

            imgSave.setOnClickListener {
                if (!areAllTracksLoaded(item)) {
                    showToast(requireContext(), getString(R.string.load_all_tracks_to_save_an_album))
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    try {
                        val isAlbumSaved = albumViewModel.getSavedAlbumState(item.id)
                        val isListenLaterSaved = albumViewModel.getListenLaterState(item.id)
                        if (isListenLaterSaved?.isListenLaterSaved == true) {
                            albumViewModel.deleteListenLaterState(item.id)
                            if (isAlbumSaved == null) {
                                albumViewModel.deleteAlbum(item.id)
                                trackViewModel.deleteTracksById(item.id)
                            }
                            imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                        }
                        if (isAlbumSaved != null) {
                            albumViewModel.deleteSavedAlbumState(item.id)
                            if (isListenLaterSaved == null) {
                                albumViewModel.deleteAlbum(item.id)
                                trackViewModel.deleteTracksById(item.id)
                            }
                            imgSave.buttonAnimation(R.drawable.ic_favorite_border)
                        } else {
                            albumViewModel.insertAlbum(album)
                            albumViewModel.insertSavedAlbum(IsAlbumSaved(item.id, true))
                            trackViewModel.insertTracks(trackEntity)
                            imgSave.buttonAnimation(R.drawable.ic_favorite)
                        }
                        val updatedList = trackViewModel.getTracksById(album.id)
                        trackEntityAdapter.differ.submitList(updatedList)
                    } catch (e : Exception) {
                        showToast(requireContext(), "${getString(R.string.error_with_saving_album)} ${e.message}")
                    }
                }
            }

            imgListenLater.setOnClickListener {
                if (!areAllTracksLoaded(item)) {
                    showToast(requireContext(), getString(R.string.load_all_tracks_to_save_an_album))
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    try {
                        val isListenLaterSaved = albumViewModel.getListenLaterState(item.id)
                        if (isListenLaterSaved != null) {
                            albumViewModel.deleteListenLaterState(item.id)
                            val isAlbumSaved = albumViewModel.getSavedAlbumState(item.id)
                            if (isAlbumSaved == null) {
                                albumViewModel.deleteAlbum(item.id)
                                trackViewModel.deleteTracksById(item.id)
                            }
                            imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                        } else {
                            albumViewModel.insertAlbum(album)
                            albumViewModel.insertListenLaterAlbum(IsListenLaterSaved(item.id, true))
                            trackViewModel.insertTracks(trackEntity)
                            imgListenLater.buttonAnimation(R.drawable.ic_listen_later)
                        }
                        val updatedList = trackViewModel.getTracksById(album.id)
                        trackEntityAdapter.differ.submitList(updatedList)
                    } catch (e : Exception) {
                        showToast(requireContext(), "${getString(R.string.error_with_saving_album)} ${e.message}")
                    }
                }
            }
        }
    }

    private fun areAllTracksLoaded(item : AlbumDetail) : Boolean {
        val loadedTracks = trackAdapter.differ.currentList.size
        val totalTracks = item.totalTracks
        return loadedTracks > 0 && loadedTracks == totalTracks
    }

    private fun showDialog(artistId : String) {
        val customView = layoutInflater.inflate(R.layout.dialog_album_artist_list, null)

        val recyclerView = customView.findViewById<RecyclerView>(R.id.recyclerViewArtist)
        val progressBarArtist = customView.findViewById<CircularProgressIndicator>(R.id.progressBarArtist)

        recyclerView.apply {
            adapter = artistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        MaterialAlertDialogBuilder(requireContext(), R.style.AttentionDialog)
            .setPositiveButton(R.string.close) { dialog, _ -> dialog.dismiss() }
            .setView(customView)
            .show()

        artistViewModel.loadArtists(artistId)

        observeUiState(
            flow = artistViewModel.artists,
            context = requireContext(),
            lifecycleScope = lifecycleScope,
            onSuccess = { artists ->
                artistAdapter.submitList(artists)
            },
            progressBar = progressBarArtist,
            message = getString(R.string.failed_to_load_artists)
        )
    }

    private fun updateIcons(albumWithStates: AlbumWithStates) {
        val isAlbumSaved = albumWithStates.isAlbumSaved?.isAlbumSaved
        val isListenLaterSaved = albumWithStates.isListenLaterSaved?.isListenLaterSaved

        binding.apply {
            imgSave.setImageResource(
                if (isAlbumSaved == true) R.drawable.ic_favorite
                else R.drawable.ic_favorite_border
            )

            imgListenLater.setImageResource(
                if (isListenLaterSaved == true) R.drawable.ic_listen_later
                else R.drawable.ic_listen_later_border
            )
        }
    }
}