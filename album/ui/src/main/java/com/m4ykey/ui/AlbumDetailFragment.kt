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
import coil.load
import com.google.android.material.button.MaterialButton
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.formatAirDate
import com.m4ykey.core.views.showToast
import com.m4ykey.data.domain.model.AlbumDetail
import com.m4ykey.ui.adapter.TrackListPagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumDetailBinding
import com.m4ykey.ui.uistate.AlbumDetailUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailFragmentArgs by navArgs()
    private var bottomNavigationVisibility: BottomNavigationVisibility? = null
    private lateinit var navController: NavController
    private val viewModel: AlbumViewModel by viewModels()
    private val trackAdapter by lazy { TrackListPagingAdapter() }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()
        navController = findNavController()

        lifecycleScope.launch {
            viewModel.getAlbumById(args.albumId)
        }

        viewModel.detail.observe(viewLifecycleOwner) { state ->
            handleUiState(state)
        }

        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }
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
            val albumInfo =
                "$albumType • ${formatAirDate(albumDetail.releaseDate)} • ${albumDetail.totalTracks} " + getString(
                    R.string.tracks
                )

            imgAlbum.load(image) {
                crossfade(true)
                crossfade(500)
            }

            buttonsIntents(
                id = btnAlbum.id,
                url = albumDetail.externalUrls.spotify
            )
            buttonsIntents(
                id = btnArtist.id,
                url = albumDetail.artists[0].externalUrls.spotify
            )

            txtAlbumName.text = albumDetail.name
            txtArtist.text = artistList
            txtInfo.text = albumInfo

        }
    }

    private fun buttonsIntents(url: String, id: Int) {
        val button: MaterialButton = view?.findViewById(id)!!
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
}