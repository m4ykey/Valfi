package com.m4ykey.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.m4ykey.artist.ui.R
import com.m4ykey.artist.ui.databinding.ActivityArtistBinding
import com.m4ykey.core.views.BottomNavigationVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistActivity : AppCompatActivity(), BottomNavigationVisibility {

    private lateinit var binding : ActivityArtistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val artistId = intent.getStringExtra("artistId")

        if (savedInstanceState == null) {
            val fragment = ArtistFragment.newInstance(artistId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }

    override fun showBottomNavigation() {}

    override fun hideBottomNavigation() {}
}