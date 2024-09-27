package com.m4ykey.valfi2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.data.notification.createNotificationChannel
import com.m4ykey.settings.theme.ThemeOptions
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.valfi2.preferences.DialogPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationVisibility {

    @Inject
    lateinit var themePreferences : ThemePreferences
    @Inject
    lateinit var dialogPreferences: DialogPreferences

    private val REQUEST_CODE_PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                createNotificationChannel(this)
            }
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
            val themeOption = themePreferences.getSelectedThemeOptions(this@MainActivity)
            when (themeOption) {
                ThemeOptions.Light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ThemeOptions.Dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ThemeOptions.Default -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
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
        lifecycleScope.launch { dialogPreferences.setIsDialogShow(this@MainActivity) }
    }

    override fun onStart() {
        super.onStart()
        readSelectedThemeOption()
        lifecycleScope.launch {
            val isDialogShown = dialogPreferences.isDialogAlreadyShown(this@MainActivity)
            if (!isDialogShown) {
                showInitialMaterialAlertDialog()
            }
        }
    }
}