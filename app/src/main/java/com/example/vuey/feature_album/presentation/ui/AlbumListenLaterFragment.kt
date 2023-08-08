package com.example.vuey.feature_album.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumListenLaterBinding
import com.example.vuey.feature_album.presentation.adapter.ListenLaterAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumListenLaterFragment : Fragment() {

    private var _binding : FragmentAlbumListenLaterBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AlbumViewModel by viewModels()

    private val listenLaterAdapter by lazy { ListenLaterAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumListenLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigation()

        with(binding) {
            recyclerViewAlbum.apply {
                adapter = listenLaterAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }
            lifecycleScope.launch {
                viewModel.allListenLaterAlbums.collect { album ->
                    listenLaterAdapter.submitAlbum(album)
                }
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}