package com.lyrics.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import com.lyrics.ui.databinding.FragmentLyricsBinding
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.utils.copyText
import com.m4ykey.core.views.utils.getColorFromImage
import com.m4ykey.core.views.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            viewModel.isLoading.collect {
                binding.progressbar.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    showToast(requireContext(), it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.lyrics.collect { item -> item?.let { displayLyrics(it) } }
        }

        lifecycleScope.launch {
            viewModel.track.collect { item -> item?.let { displayTrackDetail(it) } }
        }
    }

    private fun displayTrackDetail(item : Track) {
        binding.apply {
            imgOpenTrack.setOnClickListener {
                requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.externalUrls.spotify)))
            }
            loadImage(imgAlbumCover, item.album.getLargestImageUrl().toString(), requireContext())
            getColorFromImage(
                imageUrl = item.album.getLargestImageUrl().toString(),
                context = requireContext()
            ) { color ->
                constraintLayout.setBackgroundColor(color)
            }
        }
    }

    private fun displayLyrics(item : LyricsItem) {
        binding.apply {
            txtLyrics.text = item.plainLyrics
            imgCopyLyrics.setOnClickListener {
                copyText(
                    context = requireContext(),
                    text = item.plainLyrics
                )
            }
        }
    }
}