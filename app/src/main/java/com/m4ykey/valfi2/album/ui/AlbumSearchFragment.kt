package com.m4ykey.valfi2.album.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.valfi2.R
import com.m4ykey.core.views.hideBottomNavigation
import com.m4ykey.core.views.isNightMode
import com.m4ykey.valfi2.databinding.FragmentAlbumSearchBinding

class AlbumSearchFragment : Fragment() {

    private lateinit var navController : NavController
    private var _binding : FragmentAlbumSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        hideBottomNavigation(R.id.bottomNavigation)

        with(binding) {
            setupToolbar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun FragmentAlbumSearchBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = if (isNightMode) {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        }

        with(toolbar) {
            navigationIcon?.setTintList(iconTint)
            menu.findItem(R.id.imgClear).setIconTintList(iconTint)

            setNavigationOnClickListener {
                navController.navigateUp()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.imgClear -> {
                        etSearch.setText("")
                        true
                    }
                    else -> false
                }
            }
        }
    }
}