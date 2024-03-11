package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.showToast
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.ListenLaterPagingAdapter
import com.m4ykey.ui.databinding.FragmentAlbumListenLaterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumListenLaterFragment : Fragment(), OnItemClickListener<AlbumEntity> {

    private var _binding : FragmentAlbumListenLaterBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController : NavController
    private val viewModel : AlbumViewModel by viewModels()
    private val listenLaterAdapter by lazy { ListenLaterPagingAdapter(this, requireContext()) }

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
        _binding = FragmentAlbumListenLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

        with(binding) {
            setupToolbar()
            setupRecyclerView()
            getRandomAlbum()
            lifecycleScope.launch {
                val albumCount = viewModel.getListenLaterCount().firstOrNull() ?: 0
                txtAlbumCount.text = getString(R.string.album_count, albumCount)
                viewModel.albumPagingData
                    .map { pagingData -> pagingData.filter { it.isListenLaterSaved } }
                    .observe(viewLifecycleOwner) { pagingData ->
                        listenLaterAdapter.submitData(lifecycle, pagingData)
                    }
            }
        }
    }

    private fun FragmentAlbumListenLaterBinding.getRandomAlbum() {
        btnListenLater.setOnClickListener {
            lifecycleScope.launch {
                val randomAlbum = viewModel.getRandomAlbum()
                if (randomAlbum != null) {
                    val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(randomAlbum.id)
                    navController.navigate(action)
                } else {
                    showToast(requireContext(), requireContext().getString(R.string.first_add_something_to_list))
                }
            }
        }
    }

    private fun FragmentAlbumListenLaterBinding.setupRecyclerView() {
        recyclerViewListenLater.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            adapter = listenLaterAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun FragmentAlbumListenLaterBinding.setupToolbar() {
        toolbar.apply {
            setNavigationOnClickListener { navController.navigateUp() }
            menu.findItem(R.id.imgAdd).setOnMenuItemClickListener {
                val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumSearchFragment()
                navController.navigate(action)
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int, item: AlbumEntity) {
        val action = AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(item.id)
        navController.navigate(action)
    }

}