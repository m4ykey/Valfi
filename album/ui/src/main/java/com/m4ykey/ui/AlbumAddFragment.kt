package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.ui.databinding.FragmentAlbumAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumAddFragment : Fragment() {

    private lateinit var navController: NavController
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private var _binding : FragmentAlbumAddBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentAlbumAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}