package com.m4ykey.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.isNightMode
import com.m4ykey.ui.databinding.FragmentAlbumStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumStatisticsFragment : Fragment() {

    private var _binding : FragmentAlbumStatisticsBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationVisibility) {
            bottomNavigationVisibility = context
        } else {
            throw RuntimeException("$context ${getString(R.string.must_implement_bottom_navigation)}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

        with(binding) {
            setupToolbar()
        }

    }

    private fun FragmentAlbumStatisticsBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), if (isNightMode) R.color.white else R.color.black)
        )
        with(toolbar) {
            navigationIcon?.setTintList(iconTint)
            setNavigationOnClickListener { navController.navigateUp() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}