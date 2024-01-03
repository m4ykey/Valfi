package com.m4ykey.valfi2.album.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.m4ykey.valfi2.R
import com.m4ykey.core.views.hideBottomNavigation
import com.m4ykey.core.views.isNightMode
import com.m4ykey.valfi2.album.ui.adapter.SearchAlbumAdapter
import com.m4ykey.valfi2.databinding.FragmentAlbumSearchBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class AlbumSearchFragment : Fragment() {

    private lateinit var navController : NavController
    private var _binding : FragmentAlbumSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel = get<AlbumViewModel>()
    private val adapter by lazy { SearchAlbumAdapter() }

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

            lifecycleScope.launch {
                viewModel.albums.collectLatest {
                    adapter.submitData(it)
                }
            }

            rvSearchAlbums.adapter = adapter
            rvSearchAlbums.layoutManager = LinearLayoutManager(requireContext())
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