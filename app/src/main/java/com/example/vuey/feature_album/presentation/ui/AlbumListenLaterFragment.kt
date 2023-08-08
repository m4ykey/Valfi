package com.example.vuey.feature_album.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.core.common.utils.toAlbum
import com.example.vuey.core.common.utils.toAlbumEntity
import com.example.vuey.databinding.FragmentAlbumListenLaterBinding
import com.example.vuey.feature_album.data.local.source.entity.ListenLaterEntity
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

    private lateinit var currentAlbum : List<ListenLaterEntity>

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
                    if (album.isEmpty()) {
                        recyclerViewAlbum.visibility = View.GONE
                        layoutEmptyList.root.visibility = View.VISIBLE
                    }
                    listenLaterAdapter.submitAlbum(album)
                    currentAlbum = album
                }
            }
            btnRandomAlbum.setOnClickListener {
                if (::currentAlbum.isInitialized && currentAlbum.isNotEmpty()) {
                    val randomAlbum = currentAlbum.random()
                    val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(
                        albumId = randomAlbum.albumId,
                        album = currentAlbum[0].toAlbum(),
                        albumEntity = currentAlbum[0].toAlbumEntity(),
                        listenLaterEntity = currentAlbum[0],
                        isFromAlbumListenLaterFragment = true
                    )
                    it.findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.first_you_need_add_something_to_list), Toast.LENGTH_SHORT).show()
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