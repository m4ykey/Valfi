package com.example.vuey.presentation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumListenLaterBinding
import com.example.vuey.presentation.album.adapter.ListenLaterAdapter
import com.example.vuey.presentation.album.viewmodel.AlbumViewModel
import com.example.vuey.presentation.components.EmptyLaterListScreen
import com.m4ykey.common.utils.showSnackbar
import com.m4ykey.common.utils.toAlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
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
                        with(composeView) {
                            visibility = View.VISIBLE
                            setContent { EmptyLaterListScreen() }
                        }
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
                        albumEntity = randomAlbum.toAlbumEntity(),
                        listenLaterEntity = randomAlbum
                    )
                    it.findNavController().navigate(action)
                } else {
                    showSnackbar(
                        requireView(),
                        getString(R.string.first_you_need_add_something_to_list)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}