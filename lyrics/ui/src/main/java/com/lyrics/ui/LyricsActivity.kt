package com.lyrics.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lyrics.ui.databinding.ActivityLyricsBinding
import com.m4ykey.core.views.BottomNavigationVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LyricsActivity : AppCompatActivity(), BottomNavigationVisibility {

    private lateinit var binding : ActivityLyricsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLyricsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")

        if (savedInstanceState == null) {
            val fragment = LyricsFragment.newInstance(trackName, artistName)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }

    override fun showBottomNavigation() {}

    override fun hideBottomNavigation() {}
}