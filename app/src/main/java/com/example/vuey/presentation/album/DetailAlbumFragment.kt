package com.example.vuey.presentation.album

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumDetailBinding
import com.example.vuey.presentation.album.adapter.TrackListAdapter
import com.example.vuey.presentation.album.viewmodel.AlbumViewModel
import com.example.vuey.presentation.album.viewmodel.ui_state.DetailAlbumUiState
import com.google.android.material.snackbar.Snackbar
import com.m4ykey.common.network.NetworkStateMonitor
import com.m4ykey.common.utils.formatAirDate
import com.m4ykey.common.utils.hideBottomNavigation
import com.m4ykey.common.utils.showSnackbar
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import com.m4ykey.remote.album.model.spotify.album.Artist
import com.m4ykey.remote.album.model.spotify.album.ExternalUrls
import com.m4ykey.remote.album.model.spotify.album.Tracks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: AlbumViewModel by viewModels()
    private val arguments: DetailAlbumFragmentArgs by navArgs()
    private val trackListAdapter by lazy { TrackListAdapter() }
    private var isAlbumSaved = false
    private var isListenLater = false

    private lateinit var connectivityManager: ConnectivityManager
    private val networkStateMonitor: NetworkStateMonitor by lazy {
        NetworkStateMonitor(connectivityManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkStateMonitor.startMonitoring()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            observeDetailAlbum()
            hideBottomNavigation(R.id.bottomNavigation)

            val albumDatabase = arguments.albumEntity
            val listenLaterDatabase = arguments.listenLaterEntity

            lifecycleScope.launch {
                detailViewModel.getAlbumDetail(arguments.albumId)
                val album = detailViewModel.getAlbumById(albumDatabase.id).first()
                val listenLater =
                    detailViewModel.getListenLaterAlbumById(listenLaterDatabase.albumId).first()

                isAlbumSaved = album != null
                isListenLater = listenLater != null

                if (isAlbumSaved) {
                    imgSave.setImageResource(R.drawable.ic_save)
                } else {
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                }

                if (isListenLater) {
                    imgTime.setImageResource(R.drawable.ic_time)
                } else {
                    imgTime.setImageResource(R.drawable.ic_time_outline)
                }
            }

            val trackList = albumDatabase.trackList.map { trackEntity ->
                Tracks.AlbumItem(
                    id = albumDatabase.id,
                    trackName = trackEntity.trackName,
                    artistList = trackEntity.artistList.map { artistEntity ->
                        Artist(
                            id = artistEntity.id,
                            artistName = artistEntity.name,
                            externalUrls = ExternalUrls(
                                spotify = artistEntity.externalUrls.spotify
                            )
                        )
                    },
                    durationMs = trackEntity.durationMs,
                    externalUrls = ExternalUrls(spotify = albumDatabase.externalUrls.spotify),
                    albumType = albumDatabase.albumType
                )
            }
            trackListAdapter.submitTrack(trackList)

            val listenLaterEntity = ListenLaterEntity(
                albumId = listenLaterDatabase.albumId,
                albumTitle = listenLaterDatabase.albumTitle,
                albumImage = listenLaterDatabase.albumImage
            )

            val albumEntity = AlbumEntity(
                albumLength = albumDatabase.albumLength,
                albumName = albumDatabase.albumName,
                albumType = albumDatabase.albumType,
                id = albumDatabase.id,
                releaseDate = formatAirDate(albumDatabase.releaseDate).toString(),
                totalTracks = albumDatabase.totalTracks,
                externalUrls = AlbumEntity.ExternalUrlsEntity(
                    spotify = albumDatabase.externalUrls.spotify
                ),
                albumCover = albumDatabase.albumCover,
                artistList = albumDatabase.artistList,
                trackList = albumDatabase.trackList.map { track ->
                    AlbumEntity.TrackListEntity(
                        durationMs = track.durationMs,
                        trackName = track.trackName,
                        artistList = track.artistList
                    )
                }
            )

            recyclerViewTracks.adapter = trackListAdapter
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            imgSave.setOnClickListener {
                isAlbumSaved = !isAlbumSaved
                if (isAlbumSaved) {
                    showSnackbar(
                        requireView(),
                        getString(R.string.added_to_library)
                    )
                    imgSave.setImageResource(R.drawable.ic_save)
                    detailViewModel.insertAlbum(albumEntity)
                } else {
                    showSnackbar(
                        requireView(),
                        getString(R.string.removed_from_library)
                    )
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                    detailViewModel.deleteAlbum(albumEntity)
                }
            }

            imgTime.setOnClickListener {
                isListenLater = !isListenLater
                if (isListenLater) {
                    showSnackbar(
                        requireView(),
                        getString(R.string.added_to_listen_later)
                    )
                    imgTime.setImageResource(R.drawable.ic_time)
                    detailViewModel.insertAlbumToListenLater(listenLaterEntity)
                } else {
                    showSnackbar(
                        requireView(),
                        getString(R.string.removed_from_listen_later)
                    )
                    imgTime.setImageResource(R.drawable.ic_time_outline)
                    detailViewModel.deleteAlbumToListenLater(listenLaterEntity)
                }
            }

            val albumTime = albumDatabase.albumLength
            val albumTimeHour = albumTime / (1000 * 60 * 60)
            val albumTimeMinute = (albumTime / (1000 * 60)) % 60
            val albumTimeSeconds = (albumTime / 1000) % 60

            txtAlbumTime.text = if (albumTimeHour == 0) {
                String.format(
                    "%d min %d ${getString(R.string.sec)}",
                    albumTimeMinute,
                    albumTimeSeconds
                )
            } else if (albumTimeMinute == 0) {
                String.format("%d ${getString(R.string.hour)}", albumTimeHour)
            } else {
                String.format(
                    "%d ${getString(R.string.hour)} %d min",
                    albumTimeHour,
                    albumTimeMinute
                )
            }

            txtAlbumName.text = albumDatabase.albumName
            txtArtist.text = albumDatabase.artistList.joinToString(separator = ", ") { it.name }
            txtInfo.text = "${albumDatabase.albumType.replaceFirstChar { it.uppercase() }} • " +
                    "${formatAirDate(albumDatabase.releaseDate)} • ${albumDatabase.totalTracks} " + getString(
                R.string.tracks
            )

            imgAlbum.load(albumDatabase.albumCover.url) {
                crossfade(true)
                crossfade(1000)
            }

            with(btnAlbum) {
                elevation = 0f
                setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(albumDatabase.externalUrls.spotify)
                        )
                    )
                }
            }
            with(btnArtist) {
                setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(albumDatabase.artistList[0].externalUrls.spotify)
                        )
                    )
                }
                elevation = 0f
            }
        }
    }

    private fun FragmentAlbumDetailBinding.observeDetailAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.albumDetailUiState.collect { uiState ->
                    when (uiState) {
                        is DetailAlbumUiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is DetailAlbumUiState.Failure -> {
                            progressBar.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.message,
                                Snackbar.LENGTH_LONG
                            )
                        }

                        is DetailAlbumUiState.Success -> {

                            progressBar.visibility = View.GONE

                            val albumDetail = uiState.albumData

                            val albumCover =
                                albumDetail.imageList.find { it.height == 640 && it.width == 640 }
                            val artistNames =
                                albumDetail.artistList.joinToString(separator = ", ") { it.artistName }

                            var time = 0
                            for (track in albumDetail.tracks.items) {
                                time += track.durationMs
                            }

                            val albumTime = time
                            val albumTimeHour = time / (1000 * 60 * 60)
                            val albumTimeMinute = (time / (1000 * 60)) % 60
                            val albumTimeSeconds = (time / 1000) % 60

                            val albumLength = if (albumTimeHour == 0) {
                                String.format(
                                    "%d min %d ${getString(R.string.sec)}",
                                    albumTimeMinute,
                                    albumTimeSeconds
                                )
                            } else if (albumTimeMinute == 0) {
                                String.format("%d ${getString(R.string.hour)}", albumTimeHour)
                            } else {
                                String.format(
                                    "%d ${getString(R.string.hour)} %d min",
                                    albumTimeHour,
                                    albumTimeMinute
                                )
                            }

                            txtAlbumName.text = albumDetail.albumName
                            txtAlbumTime.text = albumLength
                            txtArtist.text = artistNames

                            txtInfo.text =
                                "${albumDetail.albumType.replaceFirstChar { it.uppercase() }} • " +
                                        "${formatAirDate(albumDetail.releaseDate)} • ${albumDetail.totalTracks} " + getString(
                                    R.string.tracks
                                )

                            imgAlbum.load(albumCover?.url) {
                                crossfade(true)
                                crossfade(1000)
                                error(R.drawable.album_error)
                            }

                            btnAlbum.setOnClickListener {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(albumDetail.externalUrls.spotify)
                                    )
                                )
                            }
                            btnArtist.setOnClickListener {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(albumDetail.artistList[0].externalUrls.spotify)
                                    )
                                )
                            }

                            albumDetail.let { trackList ->
                                trackListAdapter.submitTrack(
                                    trackList.tracks.items
                                )
                            }

                            val artistEntity = albumDetail.artistList.map { artist ->
                                AlbumEntity.ArtistEntity(
                                    id = artist.id,
                                    name = artist.artistName,
                                    externalUrls = AlbumEntity.ExternalUrlsEntity(
                                        spotify = artist.externalUrls.spotify
                                    )
                                )
                            }

                            val albumEntity = AlbumEntity(
                                id = albumDetail.id,
                                albumLength = albumTime,
                                albumName = albumDetail.albumName,
                                albumType = albumDetail.albumType,
                                releaseDate = formatAirDate(albumDetail.releaseDate).toString(),
                                totalTracks = albumDetail.totalTracks,
                                externalUrls = AlbumEntity.ExternalUrlsEntity(
                                    spotify = albumDetail.externalUrls.spotify
                                ),
                                albumCover = albumDetail.imageList.firstOrNull()?.let { image ->
                                    AlbumEntity.ImageEntity(
                                        width = 640,
                                        height = 640,
                                        url = image.url
                                    )
                                }!!,
                                artistList = artistEntity,
                                trackList = albumDetail.tracks.items.map { tracks ->
                                    AlbumEntity.TrackListEntity(
                                        durationMs = tracks.durationMs,
                                        trackName = tracks.trackName,
                                        artistList = artistEntity
                                    )
                                }
                            )

                            val listenLaterEntity = ListenLaterEntity(
                                albumId = albumDetail.id,
                                albumTitle = albumDetail.albumName,
                                albumImage = albumDetail.imageList.firstOrNull()?.let { image ->
                                    ListenLaterEntity.ListenLaterImage(
                                        width = 640,
                                        height = 640,
                                        url = image.url
                                    )
                                }!!
                            )

                            imgTime.setOnClickListener {
                                isListenLater = !isListenLater
                                if (isListenLater) {
                                    showSnackbar(
                                        requireView(),
                                        getString(R.string.added_to_listen_later)
                                    )
                                    imgTime.setImageResource(R.drawable.ic_time)
                                    detailViewModel.insertAlbumToListenLater(listenLaterEntity)
                                } else {
                                    showSnackbar(
                                        requireView(),
                                        getString(R.string.removed_from_listen_later)
                                    )
                                    imgTime.setImageResource(R.drawable.ic_time_outline)
                                    detailViewModel.deleteAlbumToListenLater(listenLaterEntity)
                                }
                            }

                            imgSave.setOnClickListener {
                                isAlbumSaved = !isAlbumSaved
                                if (isAlbumSaved) {
                                    showSnackbar(
                                        requireView(),
                                        getString(R.string.added_to_library)
                                    )
                                    imgSave.setImageResource(R.drawable.ic_save)
                                    detailViewModel.insertAlbum(albumEntity)
                                } else {
                                    showSnackbar(
                                        requireView(),
                                        getString(R.string.removed_from_library)
                                    )
                                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                                    detailViewModel.deleteAlbum(albumEntity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        networkStateMonitor.stopMonitoring()
    }
}