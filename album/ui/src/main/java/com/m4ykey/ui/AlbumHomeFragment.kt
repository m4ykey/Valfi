package com.m4ykey.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
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
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.showToast
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.AlbumEntityPagingAdapter
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.databinding.FragmentAlbumHomeBinding
import com.m4ykey.ui.helpers.ListSortingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

@AndroidEntryPoint
class AlbumHomeFragment : Fragment(), OnItemClickListener<AlbumEntity> {

    private var _binding : FragmentAlbumHomeBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController : NavController
    private var isListViewChanged = false
    private val albumAdapter by lazy { AlbumEntityPagingAdapter(this) }
    private val viewModel : AlbumViewModel by viewModels()
    private var isAlbumSelected = false
    private var isEpSelected = false
    private var isSingleSelected = false
    private var isCompilationSelected = false

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

            lifecycleScope.launch {
                viewModel.albumPagingData.observe(viewLifecycleOwner) { pagingData ->
                    albumAdapter.submitData(lifecycle, pagingData)
                }
            }
            viewModel.currentViewType.observe(viewLifecycleOwner) { viewType ->
                albumAdapter.setupViewType(viewType)
            }
        }
    }

    private fun FragmentAlbumHomeBinding.setupChips() {
        chipList.apply {
            setOnClickListener {
                isListViewChanged = !isListViewChanged
                when {
                    isListViewChanged -> setChipIconResource(R.drawable.ic_grid)
                    else -> setChipIconResource(R.drawable.ic_list)
                }
                setRecyclerViewLayout(isListViewChanged)
            }
        }
        chipSortBy.setOnClickListener { listTypeDialog() }

        chipAlbum.setOnClickListener {
            isAlbumSelected = !isAlbumSelected
            if (isAlbumSelected) viewModel.getAlbumsOfTypeAlbumPaged() else viewModel.getAllAlbumsPaged()
        }
        chipCompilation.setOnClickListener {
            isCompilationSelected = !isCompilationSelected
            if (isCompilationSelected) viewModel.getAlbumsOfTypeCompilationPaged() else viewModel.getAllAlbumsPaged()
        }
        chipEp.setOnClickListener {
            isEpSelected = !isEpSelected
            if (isEpSelected) viewModel.getAlbumsOfTypeEPPaged() else viewModel.getAllAlbumsPaged()
        }
        chipSingle.setOnClickListener {
            isSingleSelected = !isSingleSelected
            if (isSingleSelected) viewModel.getAlbumsOfTypeSinglePaged() else viewModel.getAllAlbumsPaged()
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
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = albumAdapter.withLoadStateFooter(
                footer = LoadStateAdapter { albumAdapter.retry() }
            )
        }
    }

    private fun FragmentAlbumHomeBinding.listTypeDialog() {
        val sortOptions = resources.getStringArray(R.array.sort_options)

        MaterialAlertDialogBuilder(requireContext(), R.style.SortMaterialAlertDialog)
            .setTitle(R.string.sort_by)
            .setItems(sortOptions) { _, index ->
                when (index) {
                    0 -> {
                        chipSortBy.text = getString(R.string.recently_added)
                        viewModel.updateSortingType(ListSortingType.RECENTLY_ADDED)
                    }
                    1 -> {
                        chipSortBy.text = getString(R.string.alphabetical)
                        viewModel.updateSortingType(ListSortingType.ALPHABETICAL)
                    }
                }
            }
            .show()
    }

    private fun FragmentAlbumHomeBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), if (isNightMode) R.color.white else R.color.black)
        )
        val setIconColor : (MenuItem) -> Unit = { item -> item.iconTintList = iconTint }

        with(toolbar) {
            menu.apply {
                setIconColor(findItem(R.id.imgSearch))
                setIconColor(findItem(R.id.imgLink))
            }
            navigationIcon?.setTintList(iconTint)

            val buttons = listOf(
                Pair(R.id.imgSearch) {
                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumSearchFragment()
                    navController.navigate(action)
                },
                Pair(R.id.imgLink) {
                    showInsertAlbumLinkDialog()
                }
            )
            for ((itemId, action) in buttons) {
                menu.findItem(itemId)?.setOnMenuItemClickListener {
                    action.invoke()
                    true
                }
            }

            val drawerButtons = listOf(
                Pair(R.id.imgStatistics) {
                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumStatisticsFragment()
                    navController.navigate(action)
                    drawerLayout.close()
                },
                Pair(R.id.imgSettings) {  },
                Pair(R.id.imgAddAlbum) {  },
                Pair(R.id.imgListenLater) {  }
            )
            navigationView.setNavigationItemSelectedListener { menuItem ->
                val itemId = menuItem.itemId
                val action = drawerButtons.find { it.first == itemId }?.second
                action?.invoke()
                true
            }

            setNavigationOnClickListener { drawerLayout.open() }
        }
    }

    private fun showInsertAlbumLinkDialog() {
        val customView = layoutInflater.inflate(R.layout.layout_insert_album_link, null)
        val etInputLink : TextInputEditText = customView.findViewById(R.id.etInputLink)

        MaterialAlertDialogBuilder(requireContext(), R.style.EnterLinkMaterialDialogTheme)
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
    }

    private fun isValidAlbumUrl(url: String): Boolean {
        try {
            val uri = URL(url).toURI()
            if (uri.host == "open.spotify.com" && uri.path.startsWith("/album/") || uri.path.startsWith("/track/")) {
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

    override fun onItemClick(position: Int, item: AlbumEntity) {
        val albumId = item.id
        val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumDetailFragment(albumId = albumId)
        findNavController().navigate(action)
    }
}