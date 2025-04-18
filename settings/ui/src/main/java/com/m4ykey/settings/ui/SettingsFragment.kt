package com.m4ykey.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.authentication.KeyActivity
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.openUrlBrowser
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.settings.data.file.generateJsonData
import com.m4ykey.settings.data.file.insertAlbumData
import com.m4ykey.settings.data.file.readJsonData
import com.m4ykey.settings.data.file.saveJsonToFile
import com.m4ykey.settings.data.theme.ThemeOptions
import com.m4ykey.settings.data.theme.ThemePreferences
import com.m4ykey.settings.data.theme.setCompatibleWithPhoneSettings
import com.m4ykey.settings.data.theme.setDarkTheme
import com.m4ykey.settings.data.theme.setLightTheme
import com.m4ykey.settings.ui.BuildConfig.APP_VERSION
import com.m4ykey.settings.ui.databinding.FragmentSettingsBinding
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
    lateinit var repository: AlbumRepository
    @Inject
    lateinit var trackRepository: TrackRepository

    private val getContent = registerForActivityResult(CreateDocument("application/json")) { uri : Uri? ->
        uri?.let {
            lifecycleScope.launch {
                saveJsonToFile(requireActivity(), it, generateJsonData(repository, trackRepository))
            }
        }
    }

    private val getReadContent = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri : Uri? ->
        uri?.let {
            lifecycleScope.launch {
                val albumsData = readJsonData(requireContext(), it)
                albumsData?.let { data ->
                    insertAlbumData(repository, data, trackRepository)
                }
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
            linearLayoutReadData.setOnClickListener {
                getReadContent.launch(arrayOf("application/json"))
            }

            imgSpotifyLogo.setOnClickListener { openUrlBrowser(requireContext(), "https://developer.spotify.com/") }
            linearLayoutGithub.setOnClickListener { openUrlBrowser(requireContext(), "https://github.com/m4ykey/Valfi-2") }
            imgLRCLIB.setOnClickListener { openUrlBrowser(requireContext(), "https://lrclib.net/") }
            linearLayoutKey.setOnClickListener {
                val intent = Intent(requireContext(), KeyActivity::class.java)
                startActivity(intent)
            }
            linearLayoutEmail.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:".toUri()
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("valficontact@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Version: $APP_VERSION")
                    }
                    startActivity(intent)
                } catch (e : ActivityNotFoundException) {
                    showToast(requireContext(), getString(R.string.no_email_app))
                } catch (e : Exception) {
                    showToast(requireContext(), getString(R.string.error_occurred))
                }
            }

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