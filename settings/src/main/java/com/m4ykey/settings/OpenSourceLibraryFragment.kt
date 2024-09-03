package com.m4ykey.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.settings.databinding.FragmentOpenSourceLibraryBinding
import com.mikepenz.aboutlibraries.LibsBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenSourceLibraryFragment : BaseFragment<FragmentOpenSourceLibraryBinding>(
    FragmentOpenSourceLibraryBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, LibsBuilder().supportFragment())
            .commit()
    }
}