package com.m4ykey.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.settings.databinding.FragmentSettingsBinding
import com.m4ykey.settings.theme.ThemeOptions
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.settings.theme.setCompatibleWithPhoneSettings
import com.m4ykey.settings.theme.setDarkTheme
import com.m4ykey.settings.theme.setLightTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    private var selectedThemeIndex : Int = ThemeOptions.DEFAULT.index

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        binding.apply {
            toolbar.setOnClickListener { findNavController().navigateUp() }
            linearLayoutTheme.setOnClickListener { showThemeDialog() }
            linearLayoutLanguage.setOnClickListener {  }
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf(
            getString(R.string.light),
            getString(R.string.dark),
            getString(R.string.compatible_with_phone_settings)
        )

        MaterialAlertDialogBuilder(requireContext(), R.style.ThemeMaterialAlertDialog)
            .setTitle(R.string.set_theme)
            .setSingleChoiceItems(themes, selectedThemeIndex) { dialog, which ->
                val selectedThemeOption = ThemeOptions.fromIndex(which)
                selectedThemeIndex = which
                applyTheme(selectedThemeOption)
                saveSelectedThemeOptions(selectedThemeOption)
                dialog.dismiss()
            }
            .show()
    }

    private fun saveSelectedThemeOptions(themeOption : ThemeOptions) {
        lifecycleScope.launch {
            if (themeOption == ThemeOptions.DEFAULT) {
                themePreferences.deleteThemeOptions(requireContext())
            } else {
                themePreferences.saveThemeOptions(requireContext(), themeOption)
            }
        }
    }

    private fun applyTheme(theme : ThemeOptions) {
        binding.apply {
            lifecycleScope.launch {
                when (theme) {
                    ThemeOptions.LIGHT -> setLightTheme(
                        context = requireContext(),
                        imageView = imgThemeIcon,
                        textView = txtTheme
                    )
                    ThemeOptions.DARK -> setDarkTheme(
                        context = requireContext(),
                        imageView = imgThemeIcon,
                        textView = txtTheme
                    )
                    ThemeOptions.DEFAULT -> setCompatibleWithPhoneSettings(
                        context = requireContext(),
                        imageView = imgThemeIcon,
                        textView = txtTheme
                    )
                }
                selectedThemeIndex = theme.index
            }
        }
    }
}