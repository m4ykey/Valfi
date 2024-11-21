package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.m4ykey.artist.ui.databinding.FragmentArtistBinding
import com.m4ykey.core.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistFragment : BaseFragment<FragmentArtistBinding>(
    FragmentArtistBinding::inflate
) {

    private val viewModel by viewModels<ArtistViewModel>()
    //private val args by navArgs<ArtistFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // lifecycleScope.launch { viewModel.getArtist() }
    }

}