package com.m4ykey.ui

import android.content.Context
import android.content.Intent
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
import com.m4ykey.core.network.NetworkMonitor
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.buttonAnimation
import com.m4ykey.core.views.buttonsIntents
import com.m4ykey.core.views.copyName
import com.m4ykey.core.views.formatAirDate
import com.m4ykey.core.views.getColorFromImage
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.showToast
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ListenLaterEntity
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.adapter.TrackListPagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumDetailBinding
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(), OnItemClickListener<TrackItem> {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailFragmentArgs by navArgs()
    private var bottomNavigationVisibility: BottomNavigationVisibility? = null
    private lateinit var navController: NavController
    private val viewModel: AlbumViewModel by viewModels()
    private val trackAdapter by lazy { TrackListPagingAdapter(this) }
    private var isAlbumSaved = false
    private var isListenLaterSaved = false
    private val networkStateMonitor : NetworkMonitor by lazy { NetworkMonitor(requireContext()) }
    private var buttonColor : Int? = null

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

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

        with(binding) {
            setupRecyclerView()
            toolbar.setNavigationOnClickListener { navController.navigateUp() }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.apply {
                getAlbumById(args.albumId)
                getLocalAlbumById(args.albumId)
                getAlbumTracks(args.albumId)
                getListenLaterAlbum(args.albumId)

                networkStateMonitor.isInternetAvailable.collect { isInternetAvailable ->
                    if (isInternetAvailable) {
                        detail.observe(viewLifecycleOwner) { state -> handleUiState(state) }
                        tracks.observe(viewLifecycleOwner) { state -> handleTrackState(state) }
                    } else {
                        lifecycleScope.launch {
                            binding.apply {
                                progressBar.isVisible = false
                                progressBarTrack.isVisible = false
                                localAlbum.collectLatest { album -> displayAlbumFromDatabase(album) }
                                listenLaterAlbum.collectLatest { album -> displayListenLaterFromDatabase(album) }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FragmentAlbumDetailBinding.displayAlbumFromDatabase(albumEntity: AlbumEntity?) {
        albumEntity?.let { album ->
            isAlbumSaved = album.isAlbumSaved
            val albumInfo =
                "${album.albumType} • ${album.releaseDate} • ${album.totalTracks} " + getString(
                    R.string.tracks
                )

            txtAlbumName.apply {
                text = album.name
                setOnClickListener { copyName(album.name, requireContext()) }
            }
            txtArtist.text = album.artistList
            txtInfo.text = albumInfo

            loadImage(imgAlbum, album.image, requireContext())

            buttonsIntents(button = btnAlbum, url = album.albumUrl, requireContext())
            buttonsIntents(button = btnArtist, url = album.artistUrl, requireContext())

            btnAlbum.setBackgroundColor(album.color ?: 0)
            btnArtist.setBackgroundColor(album.color ?: 0)

            when {
                isAlbumSaved -> imgSave.setImageResource(R.drawable.ic_favorite)
                else -> imgSave.setImageResource(R.drawable.ic_favorite_border)
            }

            imgSave.setOnClickListener {
                isAlbumSaved = !isAlbumSaved
                lifecycleScope.launch {
                    when {
                        isAlbumSaved -> viewModel.saveAlbum(album)
                        else -> viewModel.deleteAlbum(album)
                    }
                }
                val resourceId = if (isAlbumSaved) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                buttonAnimation(imgSave, resourceId)
            }
        }
    }

    private fun FragmentAlbumDetailBinding.displayListenLaterFromDatabase(listenLater: ListenLaterEntity?) {
        listenLater?.let { album ->
            isListenLaterSaved = album.isListenLater
            val albumInfo =
                "${album.albumType} • ${album.releaseDate} • ${album.totalTracks} " + getString(
                    R.string.tracks
                )

            txtAlbumName.apply {
                text = album.name
                setOnClickListener { copyName(album.name, requireContext()) }
            }
            txtArtist.text = album.artistList
            txtInfo.text = albumInfo

            loadImage(imgAlbum, album.image, requireContext())

            buttonsIntents(button = btnAlbum, url = album.albumUrl, requireContext())
            buttonsIntents(button = btnArtist, url = album.artistUrl, requireContext())

            btnAlbum.setBackgroundColor(album.color ?: 0)
            btnArtist.setBackgroundColor(album.color ?: 0)

            when {
                isListenLaterSaved -> imgListenLater.setImageResource(R.drawable.ic_listen_later)
                else -> imgListenLater.setImageResource(R.drawable.ic_listen_later_border)
            }

            imgListenLater.setOnClickListener {
                isListenLaterSaved = !isListenLaterSaved
                lifecycleScope.launch {
                    when {
                        isListenLaterSaved -> viewModel.saveListenLater(album)
                        else -> viewModel.deleteListenLater(album)
                    }
                }
                val resourceId = if (isListenLaterSaved) R.drawable.ic_listen_later else R.drawable.ic_listen_later_border
                buttonAnimation(imgListenLater, resourceId)
            }
        }
    }

    private fun FragmentAlbumDetailBinding.setupRecyclerView() {
        with(rvTrackList) {
            adapter = trackAdapter.withLoadStateHeaderAndFooter(
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
        with(binding) {
            rvTrackList.isVisible = state?.isLoading == false && state.albumTracks != null
            progressBarTrack.isVisible = state?.isLoading == true

            state?.error?.let {
                progressBarTrack.isVisible = false
                showToast(requireContext(), it)
            }
            state?.albumTracks?.let { track ->
                lifecycleScope.launch {
                    delay(500)
                    progressBarTrack.isVisible = false
                    rvTrackList.isVisible = true
                    trackAdapter.submitData(lifecycle, track)
                }
            }
        }
    }

    private fun handleUiState(state: AlbumDetailUiState?) {
        with(binding) {
            progressBar.isVisible = state?.isLoading == true
            nestedScrollView.isVisible = state?.isLoading == false && state.albumDetail != null

            state?.error?.let {
                progressBar.isVisible = false
                showToast(requireContext(), it)
            }

            state?.albumDetail?.let {
                progressBar.isVisible = false
                displayAlbumDetail(it)
            }
        }
    }

    private fun displayAlbumDetail(albumDetail: AlbumDetail) {
        with(binding) {
            val image = albumDetail.images.maxByOrNull { it.height * it.width }?.url
            val artistList = albumDetail.artists.joinToString(", ") { it.name }
            val albumType = when {
                albumDetail.totalTracks in 2..6 && albumDetail.albumType.equals(
                    "Single",
                    ignoreCase = true
                ) -> "EP"

                else -> albumDetail.albumType.replaceFirstChar { it.uppercase() }
            }
            val formatAirDate = formatAirDate(albumDetail.releaseDate)
            val albumInfo = "$albumType • $formatAirDate • ${albumDetail.totalTracks} " + getString(
                R.string.tracks
            )

            txtAlbumName.apply {
                text = albumDetail.name
                setOnClickListener { copyName(albumDetail.name, requireContext()) }
            }
            txtArtist.text = artistList
            txtInfo.text = albumInfo

            loadImage(imgAlbum, image.toString(), requireContext())

            getColorFromImage(
                image.toString(),
                context = requireContext(),
                onColorReady = { color ->
                    btnAlbum.setBackgroundColor(color)
                    btnArtist.setBackgroundColor(color)
                    buttonColor = color
                }
            )

            buttonsIntents(button = btnAlbum, url = albumDetail.externalUrls.spotify, requireContext())
            buttonsIntents(button = btnArtist, url = albumDetail.artists[0].externalUrls.spotify, requireContext())

            imgSave.setOnClickListener {
                isAlbumSaved = !isAlbumSaved
                val album = AlbumEntity(
                    albumType = albumType,
                    artistList = artistList,
                    image = image ?: "",
                    totalTracks = albumDetail.totalTracks,
                    name = albumDetail.name,
                    releaseDate = formatAirDate ?: "",
                    id = albumDetail.id,
                    isAlbumSaved = isAlbumSaved,
                    albumUrl = albumDetail.externalUrls.spotify,
                    artistUrl = albumDetail.artists[0].externalUrls.spotify,
                    color = buttonColor ?: 0
                )
                lifecycleScope.launch {
                    when {
                        isAlbumSaved -> viewModel.saveAlbum(album)
                        else -> viewModel.deleteAlbum(album)
                    }
                }
                val resourceAlbumId = if (isAlbumSaved) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                buttonAnimation(imgSave, resourceAlbumId)
            }

            imgListenLater.setOnClickListener {
                isListenLaterSaved = !isListenLaterSaved
                val album = ListenLaterEntity(
                    albumType = albumType,
                    artistList = artistList,
                    image = image ?: "",
                    totalTracks = albumDetail.totalTracks,
                    name = albumDetail.name,
                    releaseDate = formatAirDate ?: "",
                    id = albumDetail.id,
                    isListenLater = isListenLaterSaved,
                    albumUrl = albumDetail.externalUrls.spotify,
                    artistUrl = albumDetail.artists[0].externalUrls.spotify,
                    color = buttonColor ?: 0
                )
                lifecycleScope.launch {
                    when {
                        isListenLaterSaved -> viewModel.saveListenLater(album)
                        else -> viewModel.deleteListenLater(album)
                    }
                }
                val resourceListenLaterId = if (isListenLaterSaved) R.drawable.ic_listen_later else R.drawable.ic_listen_later_border
                buttonAnimation(imgListenLater, resourceListenLaterId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        networkStateMonitor.stopMonitoring()
    }

    override fun onItemClick(position: Int, item: TrackItem) {
        val trackId = item.externalUrls.spotify
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(trackId)))
    }
}