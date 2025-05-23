package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import com.google.android.material.chip.Chip
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.ui.databinding.FragmentRssHomeBinding

class RssHomeFragment : BaseFragment<FragmentRssHomeBinding>(
    FragmentRssHomeBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            efabAdd.setOnClickListener {
                val chip = Chip(requireContext()).apply {
                    text = "Test Chip"
                    isCheckable = true
                    isClickable = true
                    isCloseIconVisible = true

                    setOnCloseIconClickListener {
                        chipGroup.removeView(this)
                    }
                }
                chipGroup.addView(chip)
            }
        }
    }
}