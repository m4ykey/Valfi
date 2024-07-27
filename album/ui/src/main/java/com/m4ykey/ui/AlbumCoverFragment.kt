package com.m4ykey.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.utils.copyText
import com.m4ykey.ui.adapter.ColorAdapter
import com.m4ykey.ui.colors.ColorList
import com.m4ykey.ui.colors.extractColorsFromBitmap
import com.m4ykey.ui.colors.intToHex
import com.m4ykey.ui.colors.loadImageWithColors
import com.m4ykey.ui.databinding.FragmentAlbumCoverBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumCoverFragment : BaseFragment<FragmentAlbumCoverBinding>(
    FragmentAlbumCoverBinding::inflate
) {

    private val args by navArgs<AlbumCoverFragmentArgs>()
    private val colorAdapter by lazy { createColorAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            loadImageWithColors(imgAlbum, args.imgUrl, requireContext()) { bitmap ->
                val colors = extractColorsFromBitmap(bitmap)
                colorAdapter.submitList(colors.map { ColorList(color = it) })
            }

            recyclerViewColors.adapter = colorAdapter
        }
    }

    private fun createColorAdapter() : ColorAdapter {
        return ColorAdapter(
            onColorClick = { color ->
                val colorToHex = intToHex(color.color)
                copyText(colorToHex, requireContext(), colorToHex)
            }
        )
    }
}