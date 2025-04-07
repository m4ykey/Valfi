package com.m4ykey.valfi2

import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.animation.animationPropertiesY
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.show
import com.m4ykey.settings.data.theme.ThemeOptions
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var themePreferences : ThemePreferences
    @Inject
    lateinit var dialogPreferences: DialogPreferences

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                MusicNotificationState.title
            ) { artist, title ->
                updateCurrentlyPlayingSong(
                    artist = artist,
                    title = title
                )
            }.collect {  }
        }

        binding.apply {
            imgArrowUp.hide()
            layoutCurrentlyPlaying.imgArrowDown.setOnClickListener {
                layoutCurrentlyPlaying.root.animationPropertiesY(200f, 1f, DecelerateInterpolator())
                lifecycleScope.launch {
                    delay(1250L)
                    imgArrowUp.apply {
                        animationPropertiesY(-15f, 1f, DecelerateInterpolator())
                        show()
                    }
                }
            }

            imgArrowUp.setOnClickListener {
                lifecycleScope.launch {
                    delay(1000L)
                    layoutCurrentlyPlaying.root.animationPropertiesY(-5f, 1f, DecelerateInterpolator())
                }
                imgArrowUp.animationPropertiesY(100f, 1f, DecelerateInterpolator())
            }
        }
    }

    private fun displayGrantAccessMaterialDialog() {
        MaterialAlertDialogBuilder(this, R.style.AttentionDialog)
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
        artist : String?
    ) {
        if (title.isNullOrBlank() && artist.isNullOrBlank()) {
            binding.apply {
                layoutCurrentlyPlaying.root.isVisible = false
                imgArrowUp.isVisible = false
            }
        } else {
            binding.layoutCurrentlyPlaying.apply {
                root.isVisible = true
                txtTitle.apply {
                    text = title
                    isSelected = true
                }
                txtArtist.apply {
                    text = artist
                    isSelected = true
                }

                val currentAppPackageName = getCurrentMusicPackage() ?: ""

                val (bgColorFromPackage, strokeColorFromPackage) = getPackageColors(currentAppPackageName)

                root.setCardBackgroundColor(getColor(bgColorFromPackage))
                root.strokeColor = getColor(strokeColorFromPackage)
            }
            binding.imgArrowUp.isVisible = true

        }
    }

    private fun getPackageColors(packageName : String) : Pair<Int, Int> {
        val packageColors = mapOf(
            SPOTIFY_PACKAGE_NAME to Pair(R.color.spotify_background, R.color.spotify_stroke_color),
            APPLE_MUSIC_PACKAGE_NAME to Pair(R.color.apple_music_background, R.color.apple_music_stroke_color),
            TIDAL_PACKAGE_NAME to Pair(R.color.tidal_background, R.color.tidal_stroke_color),
            YOUTUBE_MUSIC_PACKAGE_NAME to Pair(R.color.yt_music_background, R.color.yt_music_stroke_color),
            DEEZER_PACKAGE_NAME to Pair(R.color.deezer_background, R.color.deezer_stroke_color),
            SOUNDCLOUD_PACKAGE_NAME to Pair(R.color.soundcloud_background, R.color.soundcloud_stroke_color),
            PANDORA_PACKAGE_NAME to Pair(R.color.pandora_background, R.color.pandora_stroke_color)
        )
        return packageColors[packageName] ?: Pair(R.color.gray, R.color.white)
    }

    private fun readSelectedThemeOption() {
        lifecycleScope.launch {
            themePreferences.getSelectedThemeOptions().collect { themeOptions ->
                when (themeOptions) {
                    ThemeOptions.Light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    ThemeOptions.Dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    ThemeOptions.Default -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
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