package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.loadImage
import com.m4ykey.ui.databinding.FragmentAlbumCoverBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlbumCoverFragment : BaseFragment<FragmentAlbumCoverBinding>(
    FragmentAlbumCoverBinding::inflate
) {

    private val args by navArgs<AlbumCoverFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        loadImage(binding.imgAlbum, args.imgUrl, requireContext())
    }

}