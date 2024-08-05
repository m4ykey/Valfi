package com.m4ykey.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.settings.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        binding.apply {
            toolbar.setOnClickListener { findNavController().navigateUp() }
            linearLayoutTheme.setOnClickListener {  }
            linearLayoutLanguage.setOnClickListener {  }
        }
    }

}