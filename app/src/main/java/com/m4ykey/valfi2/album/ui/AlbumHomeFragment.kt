package com.m4ykey.valfi2.album.ui

import android.content.res.ColorStateList
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.valfi2.R
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.showBottomNavigation
import com.m4ykey.core.views.showToast
import com.m4ykey.valfi2.databinding.FragmentAlbumHomeBinding

class AlbumHomeFragment : Fragment() {

    private lateinit var navController : NavController
    private var _binding : FragmentAlbumHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        showBottomNavigation(R.id.bottomNavigation)
        val displayManager = requireContext().getSystemService(DisplayManager::class.java)
        val defaultDisplay = displayManager.getDisplay(Display.DEFAULT_DISPLAY)

        with(binding) {
            setupToolbar()
            when (defaultDisplay.rotation) {
                Surface.ROTATION_0, Surface.ROTATION_180 -> txtOrientation.text = "Vertical"
                else -> txtOrientation.text = "Horizontal"
            }

            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) { 
                    R.id.imgSettings -> showToast(requireContext(), "Settings")
                    R.id.imgStatistics -> showToast(requireContext(), "Statistics")
                }
                return@setNavigationItemSelectedListener true
            }
        }
    }

    private fun FragmentAlbumHomeBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = if (isNightMode) {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        }

        with(toolbar) {
            navigationIcon?.setTintList(iconTint)
            with(menu) {
                findItem(R.id.imgTime).setIconTintList(iconTint)
                findItem(R.id.imgSearch).setIconTintList(iconTint)
            }

            setNavigationOnClickListener {
                drawerLayout.open()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.imgSearch -> {
                        navController.navigate(R.id.action_albumHomeFragment_to_albumSearchFragment)
                        true
                    }
                    R.id.imgTime -> {
                        showToast(requireContext(), "Listen later")
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}