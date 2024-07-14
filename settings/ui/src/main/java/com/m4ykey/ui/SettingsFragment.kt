package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.ui.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    private var selectedThemeOptions : ThemeOptions = ThemeOptions.DEFAULT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            linearLayoutTheme.setOnClickListener { setThemeOptions() }
            linearLayoutCache.setOnClickListener { openClearCacheDialog() }
        }
    }

    private fun openClearCacheDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.clear_cache)
            .setMessage(R.string.sure_clear_cache)
            .setPositiveButton(R.string.clear) { _, _ -> }
            .setNegativeButton(R.string.close) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun setThemeOptions() {
        val themeOptions = ThemeOptions.entries
        val items = themeOptions.map { requireContext().getString(it.displayName) }.toTypedArray()
        var selectedIndexOptions = themeOptions.indexOf(selectedThemeOptions)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.choose_theme)
            .setSingleChoiceItems(items, selectedIndexOptions) { _, which ->
                selectedIndexOptions = which
            }
            .setPositiveButton(R.string.save) { dialog, _ ->
                val selectedTheme = themeOptions[selectedIndexOptions]
                setupThemeWithThemeOptions(selectedTheme)
                dialog.dismiss()
            }
            .show()
    }

    private fun setupThemeWithThemeOptions(themeOptions: ThemeOptions) {
        binding.apply {
            when (themeOptions) {
                ThemeOptions.DEFAULT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    txtTheme.text = getString(R.string.default_theme)
                }
                ThemeOptions.DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    txtTheme.text = getString(R.string.dark_theme)
                }
                ThemeOptions.LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    txtTheme.text = getString(R.string.light_theme)
                }
            }
            selectedThemeOptions = themeOptions
        }
    }
}