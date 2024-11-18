package com.m4ykey.valfi2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.settings.theme.ThemeOptions
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.ui.AlbumNewReleaseFragment
import com.m4ykey.valfi2.Utils.ALBUM_NEW_RELEASE_FRAGMENT
import com.m4ykey.valfi2.Utils.CUSTOM_START_SERVICE_ACTION
import com.m4ykey.valfi2.Utils.OPEN_FRAGMENT
import com.m4ykey.valfi2.databinding.ActivityMainBinding
import com.m4ykey.valfi2.notification.MusicNotificationState
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

    private fun displayCurrentlyPlayingSong() {
        lifecycleScope.launch {
            combine(MusicNotificationState.artist, MusicNotificationState.title) { artist, title ->
                updateCurrentlyPlayingSong(artist = artist, title = title)
            }.collect {  }
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

    private fun updateCurrentlyPlayingSong(title : String?, artist : String?) {
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
            }
            binding.imgArrowUp.isVisible = true

        }
    }

    private fun openFragment(fragmentName : String) {
        val fragment = when (fragmentName) {
            ALBUM_NEW_RELEASE_FRAGMENT -> AlbumNewReleaseFragment()
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, it)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottomnavigation)?.setupWithNavController(navController)
    }

    override fun showBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomnavigation)?.isVisible = true
    }

    override fun hideBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomnavigation)?.isVisible = false
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

        intent.getStringExtra(OPEN_FRAGMENT)?.let { fragmentName ->
            openFragment(fragmentName)
        }

        readSelectedThemeOption()
        readDialogPreferences()
    }
}