package com.example.vuey.feature_music_player.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.vuey.databinding.FragmentSelectMusicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectMusicFragment : Fragment() {

    private var _binding : FragmentSelectMusicBinding? = null
    private val binding get() = _binding!!

    private val args : SelectMusicFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}