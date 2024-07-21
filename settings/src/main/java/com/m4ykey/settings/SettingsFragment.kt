package com.m4ykey.settings

import android.os.Bundle
import android.view.View
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.settings.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}