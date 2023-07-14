package com.example.vuey.feature_artist.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.vuey.databinding.FragmentArtistBinding
import com.example.vuey.feature_artist.presentation.viewmodel.ArtistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistFragment : Fragment() {

    private val artistViewModel : ArtistViewModel by viewModels()

    private var _binding : FragmentArtistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
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