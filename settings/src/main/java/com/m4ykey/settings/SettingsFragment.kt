package com.m4ykey.settings

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.openUrlBrowser
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.settings.BuildConfig.APP_VERSION
import com.m4ykey.settings.databinding.FragmentSettingsBinding
import com.m4ykey.settings.file.generateJsonData
import com.m4ykey.settings.file.saveJsonToFile
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

    private var selectedThemeIndex : Int = ThemeOptions.Default.index

    @Inject
    lateinit var themePreferences: ThemePreferences

    @Inject
    lateinit var albumDao: AlbumDao

    private val getContent = registerForActivityResult(CreateDocument("application/json")) { uri : Uri? ->
        uri?.let {
            lifecycleScope.launch {
                saveJsonToFile(requireActivity(), it, generateJsonData(albumDao))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.setOnClickListener { activity?.finish() }
            linearLayoutTheme.setOnClickListener { showThemeDialog() }
            linearLayoutSaveData.setOnClickListener {
                getContent.launch("albums.json")
            }
            linearLayoutReadData.setOnClickListener {  }

            imgNewsApiLogo.setOnClickListener { openUrlBrowser(requireContext(), "https://newsapi.org/") }
            imgSpotifyLogo.setOnClickListener { openUrlBrowser(requireContext(), "https://developer.spotify.com/") }
            linearLayoutGithub.setOnClickListener { openUrlBrowser(requireContext(), "https://github.com/m4ykey/Valfi-2") }

            txtVersion.text = APP_VERSION
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf(
            getString(R.string.light),
            getString(R.string.dark),
            getString(R.string.compatible_with_device_settings)
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
            if (themeOption == ThemeOptions.Default) {
                themePreferences.deleteThemeOptions()
            } else {
                themePreferences.saveThemeOptions(themeOption)
            }
        }
    }

    private fun applyTheme(theme : ThemeOptions) {
        binding.apply {
            lifecycleScope.launch {
                when (theme) {
                    ThemeOptions.Light -> setLightTheme(
                        context = requireContext(),
                        imageView = imgThemeIcon,
                        textView = txtTheme
                    )
                    ThemeOptions.Dark -> setDarkTheme(
                        context = requireContext(),
                        imageView = imgThemeIcon,
                        textView = txtTheme
                    )
                    ThemeOptions.Default -> setCompatibleWithPhoneSettings(
                        context = requireContext(),
                        imageView = imgThemeIcon,
                        textView = txtTheme
                    )
                }
                selectedThemeIndex = theme.index
            }
        }
    }

    private fun readSelectedOptions() {
        binding.apply {
            lifecycleScope.launch {
                themePreferences.getSelectedThemeOptions().collect { selectedOptions ->
                    selectedThemeIndex = selectedOptions.index
                    when (selectedOptions) {
                        ThemeOptions.Dark -> {
                            txtTheme.text = getString(R.string.dark)
                            imgThemeIcon.setImageResource(R.drawable.ic_moon)
                        }
                        ThemeOptions.Light -> {
                            txtTheme.text = getString(R.string.light)
                            imgThemeIcon.setImageResource(R.drawable.ic_sun)
                        }
                        else -> {
                            val nightModeFlags = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                            val isNightMode = when (nightModeFlags) {
                                Configuration.UI_MODE_NIGHT_YES -> true
                                Configuration.UI_MODE_NIGHT_NO -> false
                                else -> false
                            }

                            if (isNightMode) imgThemeIcon.setImageResource(R.drawable.ic_moon) else imgThemeIcon.setImageResource(R.drawable.ic_sun)
                            txtTheme.text = getString(R.string.compatible_with_device_settings)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        readSelectedOptions()
    }
}