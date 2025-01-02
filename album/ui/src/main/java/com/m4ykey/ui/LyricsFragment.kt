package com.m4ykey.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.m4ykey.album.ui.databinding.FragmentLyricsBinding
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.ui.viewmodel.LyricsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LyricsFragment : BaseFragment<FragmentLyricsBinding>(
    FragmentLyricsBinding::inflate
) {

    private val args by navArgs<LyricsFragmentArgs>()
    private val lyricsViewModel by viewModels<LyricsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        lyricsViewModel.fetchSongLyrics(args.artist, args.song)

        lifecycleScope.launch {
            lyricsViewModel.songLyrics.collect {
                lyrics -> displayLyrics(lyrics)
            }
        }
        lifecycleScope.launch {
            lyricsViewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    showToast(requireContext(), it)
                    Log.i("LyricsError", it)
                }
            }
        }
    }

    private fun displayLyrics(text : String) {
        binding.txtLyrics.text = text
    }

}