package com.m4ykey.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.showToast
import com.m4ykey.ui.adapter.AlbumEntityPagingAdapter
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.adapter.helpers.CenterSpaceItemDecoration
import com.m4ykey.ui.adapter.helpers.convertDpToPx
import com.m4ykey.ui.adapter.navigation.OnAlbumClick
import com.m4ykey.ui.databinding.FragmentAlbumHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

@AndroidEntryPoint
class AlbumHomeFragment : Fragment(), OnAlbumClick {

    private var _binding : FragmentAlbumHomeBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController : NavController
    private var isListViewChanged = false
    private val albumAdapter by lazy { AlbumEntityPagingAdapter(this) }
    private val albumViewModel : AlbumViewModel by viewModels()

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
        _binding = FragmentAlbumHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.showBottomNavigation()
        navController = findNavController()

        with(binding) {
            setupToolbar()
            setupChips()
            setupRecyclerView()

            lifecycleScope.launch(Dispatchers.Main) {
                albumViewModel.albumPagingData.collect { pagingData ->
                    albumAdapter.submitData(pagingData)
                }
            }
            albumViewModel.currentViewType.observe(viewLifecycleOwner) { viewType ->
                albumAdapter.setupViewType(viewType)
            }
        }
    }

    private fun FragmentAlbumHomeBinding.setupChips() {
        lifecycleScope.launch {
            when {
                isListViewChanged -> { chipList.setChipIconResource(R.drawable.ic_grid) }
                else -> { chipList.setChipIconResource(R.drawable.ic_list) }
            }
            chipList.setOnClickListener {
                isListViewChanged = !isListViewChanged
                lifecycleScope.launch {
                    when {
                        isListViewChanged -> { chipList.setChipIconResource(R.drawable.ic_grid) }
                        else -> { chipList.setChipIconResource(R.drawable.ic_list) }
                    }
                    setRecyclerViewLayout(isListViewChanged)
                }
            }
            chipSortBy.setOnClickListener { listTypeDialog() }
        }
    }

    private fun FragmentAlbumHomeBinding.setRecyclerViewLayout(isListView: Boolean) {
        val newViewType = if (isListView) {
            AlbumEntityPagingAdapter.ViewType.LIST
        } else {
            AlbumEntityPagingAdapter.ViewType.GRID
        }
        albumAdapter.setupViewType(newViewType)
        rvAlbums.layoutManager = if (isListView) {
            LinearLayoutManager(requireContext())
        } else {
            GridLayoutManager(requireContext(), 3)
        }
    }

    private fun FragmentAlbumHomeBinding.setupRecyclerView() {
        with(rvAlbums) {

            val spaceBetweenItems = 10
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(spaceBetweenItems)))

            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = albumAdapter.withLoadStateFooter(
                footer = LoadStateAdapter()
            )
        }
    }

    private fun FragmentAlbumHomeBinding.listTypeDialog() {
        val sortOptions = resources.getStringArray(R.array.sort_options)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.sort_by)
            .setItems(sortOptions) { _, index ->
                when (index) {
                    0 -> { chipSortBy.text = getString(R.string.alphabetical) }
                    1 -> { chipSortBy.text = getString(R.string.recently_added) }
                }
            }
            .show()
    }

    private fun FragmentAlbumHomeBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = if (isNightMode) {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        }


        with(toolbar) {
            menu.apply {
                findItem(R.id.imgSearch).setIconTintList(iconTint)
                findItem(R.id.imgLink).setIconTintList(iconTint)
            }
            setNavigationOnClickListener { drawerLayout.open() }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.imgSearch -> {
                        val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumSearchFragment()
                        navController.navigate(action)
                        true
                    }
                    R.id.imgLink -> {
                        val customView = layoutInflater.inflate(R.layout.layout_insert_album_link, null)
                        val etInputLink : TextInputEditText = customView.findViewById(R.id.etInputLink)

                        MaterialAlertDialogBuilder(requireContext())
                            .setPositiveButton("Ok") { dialog, _ ->
                                val albumUrl = etInputLink.text.toString()
                                if (isValidAlbumUrl(albumUrl)) {
                                    val albumId = getAlbumIdFromUrl(albumUrl)
                                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumDetailFragment(albumId ?: "")
                                    navController.navigate(action)
                                } else {
                                    showToast(requireContext(), getString(R.string.invalid_album_url))
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton(getString(R.string.close)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setView(customView)
                            .show()

                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun isValidAlbumUrl(url: String): Boolean {
        try {
            val uri = URL(url).toURI()
            if (uri.host == "open.spotify.com" && uri.path.startsWith("/album/")) {
                return true
            }
        } catch (e: MalformedURLException) {
            Log.i(TAG, "Error: ${e.message.toString()}")
        } catch (e: URISyntaxException) {
            Log.i(TAG, "Error: ${e.message.toString()}")
        }
        return false
    }


    private fun getAlbumIdFromUrl(url: String): String? {
        val regex = Regex("/album/([^/?]+)")
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.getOrNull(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "AlbumHomeFragment"
    }

    override fun onAlbumClick(id: String) {
        val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumDetailFragment(albumId = id)
        findNavController().navigate(action)
    }

}