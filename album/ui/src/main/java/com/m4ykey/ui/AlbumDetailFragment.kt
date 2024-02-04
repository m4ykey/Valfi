package com.m4ykey.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import coil.load
import com.google.android.material.button.MaterialButton
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.formatAirDate
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.showToast
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.adapter.TrackListPagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumDetailBinding
import com.m4ykey.ui.uistate.AlbumDetailUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(), OnItemClickListener<TrackItem> {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailFragmentArgs by navArgs()
    private var bottomNavigationVisibility: BottomNavigationVisibility? = null
    private lateinit var navController: NavController
    private val albumViewModel: AlbumViewModel by viewModels()
    private val trackAdapter by lazy { TrackListPagingAdapter(this) }
    private var isAlbumSaved = false
    private var isListenLaterSaved = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationVisibility) {
            bottomNavigationVisibility = context
        } else {
            throw RuntimeException("$context ${getString(R.string.must_implement_bottom_navigation)}")
        }
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
        lifecycleScope.launch {
            albumViewModel.getAlbumById(args.albumId)
            albumViewModel.getLocalAlbumById(args.albumId)
        }
        albumViewModel.getAlbumTracks(args.albumId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()
        navController = findNavController()

        albumViewModel.detail.observe(viewLifecycleOwner) { state ->
            handleUiState(state)
        }

        lifecycleScope.launch {
            albumViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                trackAdapter.submitData(viewLifecycleOwner.lifecycle, tracks.albumTracks!!)
            }
        }

        with(binding) {
            setupRecyclerView()
            setupToolbar()
            albumViewModel.localAlbum.observe(viewLifecycleOwner) { album ->
                displayAlbumFromDatabase(album)
            }
        }
    }

    private fun FragmentAlbumDetailBinding.displayAlbumFromDatabase(albumEntity: AlbumEntity?) {
        albumEntity?.let { album ->
            var isAlbumSaved = album.isAlbumSaved
            val albumInfo =
                "${album.albumType} • ${album.releaseDate} • ${album.totalTracks} " + getString(
                    R.string.tracks
                )
            txtAlbumName.text = album.name
            txtArtist.text = album.artistList
            txtInfo.text = albumInfo
            imgAlbum.load(album.image) {
                crossfade(true)
                crossfade(500)
            }
            buttonsIntents(button = btnAlbum, url = album.albumUrl)
            buttonsIntents(button = btnArtist, url = album.artistUrl)

            when {
                isAlbumSaved -> imgSave.setImageResource(R.drawable.ic_favorite)
                else -> imgSave.setImageResource(R.drawable.ic_favorite_border)
            }

            imgSave.setOnClickListener {
                isAlbumSaved = !isAlbumSaved
                val resourceId = if (isAlbumSaved) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                buttonAnimation(imgSave, resourceId)
                lifecycleScope.launch {
                    when {
                        isAlbumSaved -> albumViewModel.insertAlbum(album)
                        else -> albumViewModel.deleteAlbum(album)
                    }
                }
            }
        }
    }

    private fun FragmentAlbumDetailBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), if (isNightMode) R.color.white else R.color.black)
        )

        with(toolbar) {
            navigationIcon?.setTintList(iconTint)
            setNavigationOnClickListener { navController.navigateUp() }
        }
    }

    private fun FragmentAlbumDetailBinding.setupRecyclerView() {
        with(rvTrackList) {
            adapter = trackAdapter.withLoadStateFooter(
                footer = LoadStateAdapter()
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

    private fun handleUiState(state: AlbumDetailUiState?) {
        with(binding) {
            progressBar.isVisible = state?.isLoading == true
            nestedScrollView.isVisible = !state?.isLoading!!
            when {
                state.error != null -> {
                    progressBar.isVisible = false
                    showToast(requireContext(), state.error)
                }
                state.albumDetail != null -> {
                    progressBar.isVisible = false
                    displayAlbumDetail(state.albumDetail)
                }
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
            val albumInfo =
                "$albumType • $formatAirDate • ${albumDetail.totalTracks} " + getString(
                    R.string.tracks
                )

            imgAlbum.load(image) {
                crossfade(true)
                crossfade(500)
            }

            buttonsIntents(button = btnAlbum, url = albumDetail.externalUrls.spotify)
            buttonsIntents(button = btnArtist, url = albumDetail.artists[0].externalUrls.spotify)

            imgSave.setOnClickListener {
                isAlbumSaved = !isAlbumSaved
                val resourceId = if (isAlbumSaved) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                buttonAnimation(imgSave, resourceId)

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
                    artistUrl = albumDetail.artists[0].externalUrls.spotify
                )

                lifecycleScope.launch {
                    when {
                        isAlbumSaved -> albumViewModel.insertAlbum(album)
                        else -> albumViewModel.deleteAlbum(album)
                    }
                }
            }

            imgListenLater.setOnClickListener {
                isListenLaterSaved = !isListenLaterSaved
                val resourceId = if (isListenLaterSaved) R.drawable.ic_listen_later else R.drawable.ic_listen_later_border
                buttonAnimation(imgListenLater, resourceId)
            }

            txtAlbumName.text = albumDetail.name
            txtArtist.text = artistList
            txtInfo.text = albumInfo

        }
    }

    private fun buttonsIntents(button: MaterialButton, url: String) {
        button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "DetailAlbumFragment"
    }

    private fun buttonAnimation(imageView : ImageView, resourceId : Int) {
        imageView.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                imageView.setImageResource(resourceId)
                imageView.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    override fun onItemClick(position: Int, item: TrackItem) {
        val trackId = item.externalUrls.spotify
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(trackId)))
    }
}