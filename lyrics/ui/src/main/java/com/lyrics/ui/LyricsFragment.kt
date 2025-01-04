package com.lyrics.ui

import android.os.Bundle
import android.view.View
import com.lyrics.ui.databinding.FragmentLyricsBinding
import com.m4ykey.core.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LyricsFragment : BaseFragment<FragmentLyricsBinding>(
    FragmentLyricsBinding::inflate
) {

    companion object {
        private const val ARG_TRACK_NAME = "trackName"
        private const val ARG_ARTIST_NAME = "artistName"

        fun newInstance(trackName : String, artistName : String) : LyricsFragment {
            val fragment = LyricsFragment()
            val args = Bundle().apply {
                putString(ARG_ARTIST_NAME, artistName)
                putString(ARG_TRACK_NAME, trackName)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}