package com.m4ykey.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.m4ykey.core.network.NetworkMonitor
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.buttonAnimation
import com.m4ykey.core.views.buttonsIntents
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.utils.copyName
import com.m4ykey.core.views.utils.formatAirDate
import com.m4ykey.core.views.utils.getColorFromImage
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.adapter.TrackAdapter
import com.m4ykey.ui.databinding.FragmentAlbumDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : BaseFragment<FragmentAlbumDetailBinding>(
    FragmentAlbumDetailBinding::inflate
) {

    private val args by navArgs<AlbumDetailFragmentArgs>()
    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var trackAdapter : TrackAdapter
    private val networkStateMonitor : NetworkMonitor by lazy { NetworkMonitor(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStateMonitor.startMonitoring()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        setupRecyclerView()
        binding?.toolbar?.setNavigationOnClickListener { findNavController().navigateUp() }
        lifecycleScope.launch {
            networkStateMonitor.isInternetAvailable.collect { isInternetAvailable ->
                if (isInternetAvailable) {
                    viewModel.getAlbumById(args.albumId)
                    viewModel.getAlbumTracks(args.albumId)

                    viewModel.detail.observe(viewLifecycleOwner) { items ->
                        displayAlbumDetail(items)
                    }
                    viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                        trackAdapter.submitList(tracks)
                    }

                    viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                        binding?.progressbar?.isVisible = isLoading
                    }

                    viewModel.isLoadingTracks.observe(viewLifecycleOwner) { isLoading ->
                        if (!viewModel.isLoading.value!!) {
                            binding?.progressbar?.isVisible = isLoading
                        }
                    }

                    viewModel.isError.observe(viewLifecycleOwner) { isError ->
                        if (isError) showToast(requireContext(), "Error loading data")
                    }
                } else {
                    viewModel.getAlbum(args.albumId)?.let { album -> displayAlbumFromDatabase(album) }
                }
            }
        }
    }

    private fun displayAlbumFromDatabase(album: AlbumEntity) {
        binding?.apply {
            with(album) {
                lifecycleScope.launch {
                    val albumWithStates = viewModel.getAlbumWithStates(id)
                    if (albumWithStates != null) {
                        updateIcons(albumWithStates)
                    }
                }

                loadImage(imgAlbum, images, requireContext())
                getColorFromImage(
                    images,
                    context = requireContext(),
                    onColorReady = { color ->
                        btnAlbum.setBackgroundColor(color)
                        btnArtist.setBackgroundColor(color)
                    }
                )

                buttonsIntents(button = btnArtist, url = artists[0].url, requireContext())
                buttonsIntents(button = btnAlbum, url = albumUrl, requireContext())

                txtAlbumName.apply {
                    text = name
                    setOnClickListener { copyName(name, requireContext()) }
                }
                txtArtist.text = artists.joinToString(separator = ", ") { it.name }
                txtInfo.text = getString(R.string.album_info, albumType, releaseDate, totalTracks, getString(R.string.tracks))

                imgSave.setOnClickListener {
                    lifecycleScope.launch {
                        val isAlbumSaved = viewModel.getSavedAlbumState(id)
                        val isListenLaterSaved = viewModel.getListenLaterState(id)
                        if (isListenLaterSaved?.isListenLaterSaved == true) {
                            viewModel.deleteListenLaterState(id)
                            if (isAlbumSaved == null) {
                                viewModel.deleteAlbum(id)
                            }
                            imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                        }
                        if (isAlbumSaved != null) {
                            viewModel.deleteSavedAlbumState(id)
                            if (isListenLaterSaved == null) {
                                viewModel.deleteAlbum(id)
                            }
                            imgSave.buttonAnimation(R.drawable.ic_favorite_border)
                        } else {
                            viewModel.insertAlbum(album)
                            viewModel.insertSavedAlbum(IsAlbumSaved(id, true))
                            imgSave.buttonAnimation(R.drawable.ic_favorite)
                        }
                    }
                }

                imgListenLater.setOnClickListener {
                    lifecycleScope.launch {
                        val isListenLaterSaved = viewModel.getListenLaterState(id)
                        if (isListenLaterSaved != null) {
                            viewModel.deleteListenLaterState(id)
                            val isAlbumSaved = viewModel.getSavedAlbumState(id)
                            if (isAlbumSaved == null) {
                                viewModel.deleteAlbum(id)
                            }
                            imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                        } else {
                            viewModel.insertAlbum(album)
                            viewModel.insertListenLaterAlbum(IsListenLaterSaved(id, true))
                            imgListenLater.buttonAnimation(R.drawable.ic_listen_later)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.rvTrackList?.apply {
            val onTrackClick : (TrackItem) -> Unit = { track ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(track.externalUrls.spotify)))
            }

            trackAdapter = TrackAdapter(onTrackClick)
            adapter = trackAdapter

            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        if (!viewModel.isPaginationEnded && viewModel.isLoadingTracks.value == false) {
                            lifecycleScope.launch { viewModel.getAlbumTracks(args.albumId) }
                        }
                    }
                }
            })
        }
    }

    private fun displayAlbumDetail(item: AlbumDetail) {
        binding?.apply {
            val image = item.images.maxByOrNull { it.height * it.width }?.url
            val artistList = item.artists.joinToString(", ") { it.name }
            val albumType = when {
                item.totalTracks in 2..6 && item.albumType.equals(
                    "Single",
                    ignoreCase = true
                ) -> "EP"

                else -> item.albumType.replaceFirstChar { it.uppercase() }
            }
            val formatAirDate = formatAirDate(item.releaseDate)
            val albumInfo = "$albumType • $formatAirDate • ${item.totalTracks} " + getString(
                R.string.tracks
            )
            val albumUrl = item.externalUrls.spotify
            val artistUrl = item.artists[0].externalUrls.spotify

            txtAlbumName.apply {
                text = item.name
                setOnClickListener { copyName(item.name, requireContext()) }
            }
            txtArtist.text = artistList
            txtInfo.text = albumInfo

            loadImage(imgAlbum, image.toString(), requireContext())

            getColorFromImage(
                image.toString(),
                context = requireContext()
            ) { color ->
                animateColorTransition(Color.parseColor("#4FC3F7"), color, btnAlbum, btnArtist)
            }

            buttonsIntents(button = btnAlbum, url = albumUrl ?: "", requireContext())
            buttonsIntents(button = btnArtist, url = artistUrl ?: "", requireContext())

            val artistsEntity = item.artists.map { artist ->
                ArtistEntity(
                    name = artist.name,
                    artistId = artist.id,
                    url = artistUrl.orEmpty(),
                    albumId = args.albumId
                )
            }

            val album = AlbumEntity(
                id = item.id,
                name = item.name,
                releaseDate = formatAirDate.toString(),
                totalTracks = item.totalTracks,
                images = image.toString(),
                albumType = albumType,
                artists = artistsEntity,
                albumUrl = albumUrl ?: ""
            )

            lifecycleScope.launch {
                val albumWithStates = viewModel.getAlbumWithStates(item.id)
                if (albumWithStates != null) {
                    updateIcons(albumWithStates)
                }
            }

            imgSave.setOnClickListener {
                lifecycleScope.launch {
                    val isAlbumSaved = viewModel.getSavedAlbumState(item.id)
                    val isListenLaterSaved = viewModel.getListenLaterState(item.id)
                    if (isListenLaterSaved?.isListenLaterSaved == true) {
                        viewModel.deleteListenLaterState(item.id)
                        if (isAlbumSaved == null) {
                            viewModel.deleteAlbum(item.id)
                        }
                        imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                    }
                    if (isAlbumSaved != null) {
                        viewModel.deleteSavedAlbumState(item.id)
                        if (isListenLaterSaved == null) {
                            viewModel.deleteAlbum(item.id)
                        }
                        imgSave.buttonAnimation(R.drawable.ic_favorite_border)
                    } else {
                        viewModel.insertAlbum(album)
                        viewModel.insertSavedAlbum(IsAlbumSaved(item.id, true))
                        imgSave.buttonAnimation(R.drawable.ic_favorite)
                    }
                }
            }

            imgListenLater.setOnClickListener {
                lifecycleScope.launch {
                    val isListenLaterSaved = viewModel.getListenLaterState(item.id)
                    if (isListenLaterSaved != null) {
                        viewModel.deleteListenLaterState(item.id)
                        val isAlbumSaved = viewModel.getSavedAlbumState(item.id)
                        if (isAlbumSaved == null) {
                            viewModel.deleteAlbum(item.id)
                        }
                        imgListenLater.buttonAnimation(R.drawable.ic_listen_later_border)
                    } else {
                        viewModel.insertAlbum(album)
                        viewModel.insertListenLaterAlbum(IsListenLaterSaved(item.id, true))
                        imgListenLater.buttonAnimation(R.drawable.ic_listen_later)
                    }
                }
            }
        }
    }

    private fun updateIcons(albumWithStates: AlbumWithStates) {
        val isAlbumSaved = albumWithStates.isAlbumSaved?.isAlbumSaved
        val isListenLaterSaved = albumWithStates.isListenLaterSaved?.isListenLaterSaved

        binding?.apply {
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

    private fun animateColorTransition(startColor : Int, endColor: Int, vararg buttons : MaterialButton) {
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
        colorAnimator.duration = 2500

        colorAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            buttons.forEach { it.setBackgroundColor(animatedValue) }
        }
        colorAnimator.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateMonitor.stopMonitoring()
    }
}