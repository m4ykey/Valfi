package com.example.vuey.feature_music_player.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.vuey.databinding.FragmentSelectMusicBinding
import com.example.vuey.feature_music_player.presentation.adapter.VideoAdapter
import com.example.vuey.feature_music_player.presentation.viewmodel.YoutubeViewModel
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectMusicFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentSelectMusicBinding? = null
    private val binding get() = _binding!!

    private val videoAdapter by lazy { VideoAdapter() }

    private val youtubeViewModel : YoutubeViewModel by viewModels()

    companion object {

        private const val YOUTUBE_VIDEO_NAME = "YOUTUBE_VIDEO_NAME"

        fun newInstance(musicName : String) : SelectMusicFragment {
            val fragment = SelectMusicFragment()
            val arguments = Bundle()
            arguments.putString(YOUTUBE_VIDEO_NAME, musicName)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerViewYoutubeVideo.adapter = videoAdapter
        }
        lifecycleScope.launch {
            val musicName = arguments?.getString(YOUTUBE_VIDEO_NAME)
            if (!musicName.isNullOrEmpty()) {
                youtubeViewModel.getYoutubeVideo(musicName)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                youtubeViewModel.getYoutubeVideoByNameUiState.collect { uiState ->
                    with(binding) {
                        when {
                            uiState.isLoading -> { progressBar.visibility = View.VISIBLE }
                            uiState.isError?.isNotEmpty() == true -> {
                                showSnackbar(requireView(),"${uiState.isError}", Snackbar.LENGTH_LONG)
                            }
                            uiState.youtubeVideoData.isNotEmpty() -> {
                                progressBar.visibility = View.GONE
                                videoAdapter.submitVideo(uiState.youtubeVideoData)
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
    }
}