package com.m4ykey.valfi2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.show
import com.m4ykey.settings.data.theme.ThemePreferences
import com.m4ykey.valfi2.Utils.APPLE_MUSIC_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.CUSTOM_START_SERVICE_ACTION
import com.m4ykey.valfi2.Utils.DEEZER_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.PANDORA_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.SOUNDCLOUD_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.SPOTIFY_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.TIDAL_PACKAGE_NAME
import com.m4ykey.valfi2.Utils.YOUTUBE_MUSIC_PACKAGE_NAME
import com.m4ykey.valfi2.databinding.ActivityMainBinding
import com.m4ykey.valfi2.notification.MusicNotificationState
import com.m4ykey.valfi2.notification.NotificationServiceListener
import com.m4ykey.valfi2.notification.StartServiceReceiver
import com.m4ykey.valfi2.notification.checkNotificationListenerPermission
import com.m4ykey.valfi2.preferences.DialogPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationVisibility {

    @Inject
    lateinit var themePreferences : ThemePreferences
    @Inject
    lateinit var dialogPreferences: DialogPreferences

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        val intent = Intent(this, StartServiceReceiver::class.java)
        intent.action = CUSTOM_START_SERVICE_ACTION
        sendBroadcast(intent)

        displayCurrentlyPlayingSong()
    }

    private fun getCurrentMusicPackage() : String? {
        return NotificationServiceListener.currentMusicAppPackage
    }

    private fun displayCurrentlyPlayingSong() {
        lifecycleScope.launch {
            combine(
                MusicNotificationState.artist,
                MusicNotificationState.title,
                MusicNotificationState.backgroundColor,
                MusicNotificationState.strokeColor
            ) { artist, title, background, stroke ->
                updateCurrentlyPlayingSong(
                    artist = artist,
                    title = title,
                    backgroundColor = background,
                    strokeColor = stroke
                )
            }.collect {  }
        }

        binding.apply {
            layoutCurrentlyPlaying.imgArrowDown.setOnClickListener {
                layoutCurrentlyPlaying.root.isVisible = false
                imgArrowUp.show()
            }

            imgArrowUp.setOnClickListener {
                layoutCurrentlyPlaying.root.isVisible = true
                imgArrowUp.hide()
            }
        }
    }

    private fun displayGrantAccessMaterialDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.grant_access)
            .setMessage(R.string.app_need_to_notification_display_music)
            .setPositiveButton(R.string.grant_access) { _, _ ->
                checkNotificationListenerPermission(this@MainActivity)
            }
            .setNegativeButton(R.string.dont_access_grant) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
        lifecycleScope.launch { dialogPreferences.setIsPermissionGranted() }
    }

    private fun updateCurrentlyPlayingSong(
        title : String?,
        artist : String?,
        backgroundColor : Int?,
        strokeColor : Int?
    ) {
        if (title.isNullOrBlank() && artist.isNullOrBlank()) {
            binding.apply {
                layoutCurrentlyPlaying.root.isVisible = false
                imgArrowUp.isVisible = false
            }
        } else {
            binding.layoutCurrentlyPlaying.apply {
                root.isVisible = true
                txtTitle.text = title
                txtArtist.text = artist

                val currentAppPackageName = getCurrentMusicPackage()
                when (currentAppPackageName) {
                    SPOTIFY_PACKAGE_NAME -> {
                        root.setCardBackgroundColor(getColor(R.color.spotify_background))
                        root.strokeColor = getColor(R.color.spotify_stroke_color)
                    }
                    APPLE_MUSIC_PACKAGE_NAME -> {
                        root.setCardBackgroundColor(getColor(R.color.apple_music_background))
                        root.strokeColor = getColor(R.color.apple_music_stroke_color)
                    }
                    TIDAL_PACKAGE_NAME -> {
                        root.setCardBackgroundColor(getColor(R.color.tidal_background))
                        root.strokeColor = getColor(R.color.tidal_stroke_color)
                    }
                    YOUTUBE_MUSIC_PACKAGE_NAME -> {
                        root.setCardBackgroundColor(getColor(R.color.yt_music_background))
                        root.strokeColor = getColor(R.color.yt_music_stroke_color)
                    }
                    DEEZER_PACKAGE_NAME -> {
                        root.setCardBackgroundColor(getColor(R.color.deezer_background))
                        root.strokeColor = getColor(R.color.deezer_stroke_color)
                    }
                    SOUNDCLOUD_PACKAGE_NAME -> {
                        root.setCardBackgroundColor(getColor(R.color.soundcloud_background))
                        root.strokeColor = getColor(R.color.soundcloud_stroke_color)
                    }
                    PANDORA_PACKAGE_NAME -> {
                        root.setCardBackgroundColor(getColor(R.color.pandora_background))
                        root.strokeColor = getColor(R.color.pandora_stroke_color)
                    }
                    else -> {
                        root.setCardBackgroundColor(backgroundColor ?: getColor(R.color.gray))
                        root.strokeColor = getColor(strokeColor ?: getColor(R.color.white))
                    }
                }
            }
            binding.imgArrowUp.isVisible = true

        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //findViewById<BottomNavigationView>(R.id.bottomnavigation)?.setupWithNavController(navController)
    }

    override fun showBottomNavigation() {
        //findViewById<BottomNavigationView>(R.id.bottomnavigation)?.isVisible = true
    }

    override fun hideBottomNavigation() {
        //findViewById<BottomNavigationView>(R.id.bottomnavigation)?.isVisible = false
    }

    private fun readSelectedThemeOption() {
        lifecycleScope.launch {
            themePreferences.getSelectedThemeOptions().collect { themeOptions ->
                when (themeOptions) {
                    com.m4ykey.settings.data.theme.ThemeOptions.Light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    com.m4ykey.settings.data.theme.ThemeOptions.Dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    com.m4ykey.settings.data.theme.ThemeOptions.Default -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    private fun showInitialMaterialAlertDialog() {
        val customView = layoutInflater.inflate(R.layout.layout_attention_dialog, null)

        MaterialAlertDialogBuilder(this, R.style.AttentionDialog)
            .setTitle(R.string.attention)
            .setPositiveButton(R.string.close) { dialog, _ -> dialog.dismiss() }
            .setView(customView)
            .show()
        lifecycleScope.launch { dialogPreferences.setIsDialogShow() }
    }

    private fun readDialogPreferences() {
        lifecycleScope.launch {
            if (!dialogPreferences.isDialogAlreadyShown()) {
                showInitialMaterialAlertDialog()
            }
        }

        lifecycleScope.launch {
            if (!dialogPreferences.isPermissionGranted()) {
                displayGrantAccessMaterialDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        readSelectedThemeOption()
        readDialogPreferences()
    }
}