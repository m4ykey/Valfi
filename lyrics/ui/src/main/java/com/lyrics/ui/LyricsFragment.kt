package com.lyrics.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import com.lyrics.ui.databinding.FragmentLyricsBinding
import com.m4ykey.core.network.UiState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.utils.copyText
import com.m4ykey.core.views.utils.getColorFromImage
import com.m4ykey.core.views.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.pow

@AndroidEntryPoint
class LyricsFragment : BaseFragment<FragmentLyricsBinding>(
    FragmentLyricsBinding::inflate
) {

    companion object {
        private const val ARG_TRACK_NAME = "trackName"
        private const val ARG_ARTIST_NAME = "artistName"
        private const val ARG_TRACK_ID = "trackId"

        fun newInstance(artistName : String?, trackName : String?, trackId: String?) : LyricsFragment {
            val fragment = LyricsFragment()
            val args = Bundle().apply {
                putString(ARG_TRACK_NAME, trackName)
                putString(ARG_ARTIST_NAME, artistName)
                putString(ARG_TRACK_ID, trackId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var trackName : String? = null
    private var artistName : String? = null
    private var trackId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackName = arguments?.getString(ARG_TRACK_NAME)
        artistName = arguments?.getString(ARG_ARTIST_NAME)
        trackId = arguments?.getString(ARG_TRACK_ID)
    }

    private val viewModel by viewModels<LyricsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.setOnClickListener { activity?.finish() }
            txtArtist.text = artistName
            txtTrack.text = trackName
        }

        trackName?.let { track ->
            artistName?.let { artist ->
                viewModel.searchLyrics(artistName = artist, trackName = track)
            }
        }

        trackId?.let { track -> viewModel.getTrackById(track) }

        lifecycleScope.launch {
            viewModel.lyrics.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        binding.progressbar.isVisible = false
                        uiState.data?.let { item -> displayLyrics(item) }
                    }
                    is UiState.Error -> {
                        binding.progressbar.isVisible = false
                        showToast(requireContext(), uiState.exception.message ?: "An unknown error occurred")
                    }
                    is UiState.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.track.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        binding.progressbar.isVisible = false
                        uiState.data?.let { item -> displayTrackDetail(item) }
                    }
                    is UiState.Error -> {
                        binding.progressbar.isVisible = false
                        showToast(requireContext(), uiState.exception.message ?: "An unknown error occurred")
                    }
                    is UiState.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                }
            }
        }
    }

    private fun displayTrackDetail(item : Track) {
        binding.apply {
            imgOpenTrack.setOnClickListener {
                requireContext().startActivity(Intent(Intent.ACTION_VIEW,
                    item.externalUrls.spotify.toUri()))
            }
            loadImage(imgAlbumCover, item.album.getLargestImageUrl().toString(), requireContext())
            getColorFromImage(
                imageUrl = item.album.getLargestImageUrl().toString(),
                context = requireContext()
            ) { color ->
                constraintLayout.setBackgroundColor(color)
                val luminance = calculateLuminance(color)
                if (luminance > 0.5) {
                    txtTrack.setTextColor(Color.BLACK)
                    txtArtist.setTextColor(Color.BLACK)
                    imgCopyLyrics.imageTintList = ColorStateList.valueOf(Color.BLACK)
                    imgOpenTrack.imageTintList = ColorStateList.valueOf(Color.BLACK)
                    txtLyrics.setTextColor(Color.BLACK)
                    txtNotFoundLyrics.setTextColor(Color.BLACK)
                    toolbar.setNavigationIconTint(Color.BLACK)
                } else {
                    txtTrack.setTextColor(Color.WHITE)
                    txtArtist.setTextColor(Color.WHITE)
                    imgCopyLyrics.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    imgOpenTrack.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    txtLyrics.setTextColor(Color.WHITE)
                    txtNotFoundLyrics.setTextColor(Color.WHITE)
                    toolbar.setNavigationIconTint(Color.WHITE)
                }
            }
        }
    }

    private fun calculateLuminance(color : Int) : Double {
        val red = Color.red(color) / 255.0
        val green = Color.green(color) / 255.0
        val blue = Color.blue(color) / 255.0

        val rL = if (red <= 0.03928) red / 12.92 else ((red + 0.055) / 1.055).pow(2.4)
        val gL = if (green <= 0.03928) green / 12.92 else ((green + 0.055) / 1.055).pow(2.4)
        val bL = if (blue <= 0.03928) blue / 12.92 else ((blue + 0.055) / 1.055).pow(2.4)

        return 0.2126 * rL + 0.7152 * gL + 0.0722 * bL
    }

    private fun displayLyrics(item : LyricsItem) {
        binding.apply {
            if (item.plainLyrics.isEmpty()) {
                linearLayoutEmptyLyrics.isVisible = true
                txtLyrics.isVisible = false
            } else {
                txtLyrics.text = item.plainLyrics
                linearLayoutEmptyLyrics.isVisible = false
            }
            imgCopyLyrics.setOnClickListener {
                copyText(
                    context = requireContext(),
                    text = item.plainLyrics
                )
            }
        }
    }
}