package com.example.vuey.feature_album.presentation.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumBinding
import com.example.vuey.feature_album.presentation.adapter.AlbumAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by viewModels()
    private val albumAdapter by lazy { AlbumAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            albumRecyclerView.adapter = albumAdapter
        }
        lifecycleScope.launch {
            coroutineScope {
                albumViewModel.allAlbums.collect { albums ->
                    albumAdapter.submitAlbums(albums)
                }
                albumViewModel.searchAlbumInDatabase.collect { albumList ->
                    albumAdapter.submitAlbums(albumList)
                }
            }
        }
        setupNavigation()
        showBottomNavigation()
    }

    private fun showBottomNavigation() {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.VISIBLE
    }

    private fun setupNavigation() {
        with(binding) {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.imgSearch -> {
                        linearLayoutHide.visibility = View.VISIBLE
                        imgHide.setOnClickListener {
                            linearLayoutHide.visibility = View.GONE
                        }
                        true
                    }

                    R.id.imgAdd -> {
                        findNavController().navigate(R.id.action_albumFragment_to_searchAlbumFragment)
                        true
                    }

                    R.id.imgStatistics -> {
                        findNavController().navigate(R.id.action_albumFragment_to_albumStatisticsFragment)
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
            val addItem = toolbar.menu.findItem(R.id.imgAdd)
            val searchItem = toolbar.menu.findItem(R.id.imgSearch)
            val statisticsItem = toolbar.menu.findItem(R.id.imgStatistics)
            statisticsItem.icon.let {
                MenuItemCompat.setIconTintList(statisticsItem, ColorStateList.valueOf(Color.WHITE))
            }
            searchItem.icon.let {
                MenuItemCompat.setIconTintList(searchItem, ColorStateList.valueOf(Color.WHITE))
            }
            addItem.icon.let {
                MenuItemCompat.setIconTintList(addItem, ColorStateList.valueOf(Color.WHITE))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}