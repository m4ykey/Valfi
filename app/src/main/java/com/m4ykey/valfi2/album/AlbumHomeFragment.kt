package com.m4ykey.valfi2.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.valfi2.R
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

        with(binding) {
            toolbar.setNavigationOnClickListener {
                drawerLayout.open()
            }
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.imgSearch -> {
                        navController.navigate(R.id.action_albumHomeFragment_to_albumSearchFragment)
                        true
                    }
                    else -> false
                }
            }

            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {

                }

                return@setNavigationItemSelectedListener true
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}