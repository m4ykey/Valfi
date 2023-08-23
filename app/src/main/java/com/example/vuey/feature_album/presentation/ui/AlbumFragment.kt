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
import dagger.hilt.android.AndroidEntryPoint
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
            setupNavigation()
            albumRecyclerView.adapter = albumAdapter
            lifecycleScope.launch {
                albumViewModel.allAlbums.collect { albums ->
                    if (albums.isEmpty()) {
                        albumRecyclerView.visibility = View.GONE
                        layoutEmptyList.root.visibility = View.VISIBLE
                    }
                    albumAdapter.submitAlbums(albums)
                }
            }
        }
    }

    private fun FragmentAlbumBinding.setupNavigation() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.imgLater -> {
                    findNavController().navigate(R.id.action_albumFragment_to_albumListenLaterFragment)
                    true
                }
                R.id.imgStatistics -> {
                    findNavController().navigate(R.id.action_albumFragment_to_albumStatisticsFragment)
                    true
                }
                R.id.imgAdd -> {
                    findNavController().navigate(R.id.action_albumFragment_to_searchAlbumFragment)
                    true
                }
                else -> { false }
            }
        }
        val listenLaterItem = toolbar.menu.findItem(R.id.imgLater)
        val addItem = toolbar.menu.findItem(R.id.imgAdd)
        val statisticsItem = toolbar.menu.findItem(R.id.imgStatistics)
        listenLaterItem.icon.let {
            MenuItemCompat.setIconTintList(listenLaterItem, ColorStateList.valueOf(Color.WHITE))
        }
        statisticsItem.icon.let {
            MenuItemCompat.setIconTintList(statisticsItem, ColorStateList.valueOf(Color.WHITE))
        }
        addItem.icon.let {
            MenuItemCompat.setIconTintList(addItem, ColorStateList.valueOf(Color.WHITE))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}