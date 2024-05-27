package com.m4ykey.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.google.android.material.button.MaterialButton
import com.m4ykey.core.network.NetworkMonitor
import com.m4ykey.core.views.BottomNavigationVisibility
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
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.adapter.TrackListPagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumDetailBinding
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailFragmentArgs by navArgs()
    private var bottomNavigationVisibility: BottomNavigationVisibility? = null
    private lateinit var navController: NavController
    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var trackAdapter : TrackListPagingAdapter
    private val networkStateMonitor : NetworkMonitor by lazy { NetworkMonitor(requireContext()) }
    private var isDataLoaded = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationVisibility) {
            bottomNavigationVisibility = context
        } else {
            throw RuntimeException("$context ${getString(R.string.must_implement_bottom_navigation)}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStateMonitor.startMonitoring()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

        with(binding) {
            setupRecyclerView()
            toolbar.setNavigationOnClickListener { navController.navigateUp() }
            lifecycleScope.launch {
                viewModel.apply {
                    val albumDeferred = async { getAlbumById(args.albumId) }
                    tracks.observe(viewLifecycleOwner) { state -> handleTrackState(state) }
                    getAlbumTracks(args.albumId)

                    networkStateMonitor.isInternetAvailable.collect { isInternetAvailable ->
                        if (isInternetAvailable) {
                            albumDeferred.await()
                            detail.observe(viewLifecycleOwner) { state -> handleUiState(state) }
                        } else {
                            getAlbum(args.albumId)?.let { album -> displayAlbumFromDatabase(album) }
                        }
                    }
                }
            }
        }
    }

    private fun displayAlbumFromDatabase(album: AlbumEntity) {
        with(binding) {
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

                buttonsIntents(button = btnArtist, url = artistUrl, requireContext())
                buttonsIntents(button = btnAlbum, url = albumUrl, requireContext())

                txtAlbumName.apply {
                    text = name
                    setOnClickListener { copyName(name, requireContext()) }
                }
                txtArtist.text = artists
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
        with(binding) {

            val onTrackClick : (TrackItem) -> Unit = { track ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(track.externalUrls.spotify)))
            }

            trackAdapter = TrackListPagingAdapter(onTrackClick)

            rvTrackList.adapter = trackAdapter.withLoadStateHeaderAndFooter(
                footer = LoadStateAdapter { trackAdapter.retry() },
                header = LoadStateAdapter { trackAdapter.retry() }
            )

            trackAdapter.addLoadStateListener { loadState ->
                rvTrackList.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading

                handleLoadStateError(loadState)
            }
        }
    }

    private fun handleLoadStateError(loadStates: CombinedLoadStates) {
        val errorState = loadStates.source.refresh as? LoadState.Error
        val error = errorState?.error
        if (error != null) {
            showToast(requireContext(), error.message.toString())
        }
    }

    private fun handleTrackState(state: AlbumTrackUiState?) {
        state ?: return
        with(binding) {
            progressBar.isVisible = state.isLoading && !isDataLoaded
            nestedScrollView.isVisible = !state.isLoading || isDataLoaded
            state.error?.let { showToast(requireContext(), it) }
            state.albumTracks?.let { tracks ->
                lifecycleScope.launch {
                    delay(500L)
                    trackAdapter.submitData(lifecycle, tracks)
                }
            }
        }
    }

    private fun handleUiState(state: AlbumDetailUiState?) {
        state ?: return
        with(binding) {
            progressBar.isVisible = state.isLoading && !isDataLoaded
            nestedScrollView.isVisible = !state.isLoading || isDataLoaded
            state.error?.let { showToast(requireContext(), it) }
            state.albumDetail?.let { detail ->
                lifecycleScope.launch {
                    delay(200L)
                    displayAlbumDetail(detail)
                }
            }
        }
    }

    private fun displayAlbumDetail(item: AlbumDetail) {
        lifecycleScope.launch {
            with(binding) {
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

                val album = AlbumEntity(
                    id = item.id,
                    name = item.name,
                    releaseDate = formatAirDate.toString(),
                    totalTracks = item.totalTracks,
                    images = image.toString(),
                    albumType = albumType,
                    artists = artistList,
                    albumUrl = albumUrl ?: "",
                    artistUrl = artistUrl ?: ""
                )

                val albumWithStates = viewModel.getAlbumWithStates(item.id)
                if (albumWithStates != null) {
                    updateIcons(albumWithStates)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}