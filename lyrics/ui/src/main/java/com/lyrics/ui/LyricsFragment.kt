package com.lyrics.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.ui.databinding.FragmentLyricsBinding
import com.m4ykey.core.views.BaseFragment
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

        fun newInstance(artistName : String?, trackName : String?) : LyricsFragment {
            val fragment = LyricsFragment()
            val args = Bundle().apply {
                putString(ARG_TRACK_NAME, trackName)
                putString(ARG_ARTIST_NAME, artistName)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var trackName : String? = null
    private var artistName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackName = arguments?.getString(ARG_TRACK_NAME)
        artistName = arguments?.getString(ARG_ARTIST_NAME)
    }

    private val viewModel by viewModels<LyricsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackName?.let { track ->
            artistName?.let { artist ->
                viewModel.searchLyrics(artistName = artist, trackName = track)
                Log.i("Argumenty", "Artist $artist, Track $track")
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.progressbar.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    showToast(requireContext(), it)
                    Log.i("LyricsFragment", "Error: $it")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.lyrics.collect { item -> item?.let { displayLyrics(it) } }
        }
    }

    private fun displayLyrics(item : LyricsItem) {
        binding.txtLyrics.text = item.plainLyrics
    }

}