package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.m4ykey.core.Constants.ALBUM
import com.m4ykey.core.Constants.COMPILATION
import com.m4ykey.core.Constants.EP
import com.m4ykey.core.Constants.SINGLE
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.DataManager
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.show
import com.m4ykey.core.views.showToast
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.AlbumPagingAdapter
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.databinding.FragmentAlbumHomeBinding
import com.m4ykey.ui.helpers.ViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
    private val viewModel : AlbumViewModel by viewModels()
    private val albumAdapter by lazy { AlbumPagingAdapter(this) }
    private var isSearchEditTextVisible = false
    private var isAlbumSelected = false
    private var isEPSelected = false
    private var isSingleSelected = false
    private var isCompilationSelected = false
    private lateinit var dataManager: DataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navController = findNavController()
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
        dataManager = DataManager(requireContext())

        lifecycleScope.launch {
            val selectedAlbum = dataManager.selectedAlbumType.first()
            when (selectedAlbum) {
                ALBUM -> {
                    isAlbumSelected = true
                    viewModel.getAlbumType(ALBUM)
                }
                EP -> {
                    isEPSelected = true
                    viewModel.getAlbumType(EP)
                }
                SINGLE -> {
                    isSingleSelected = true
                    viewModel.getAlbumType(SINGLE)
                }
                COMPILATION -> {
                    isCompilationSelected = true
                    viewModel.getAlbumType(COMPILATION)
                }
                else -> viewModel.getSavedAlbums()
            }

            updateChipSelection()
        }

        with(binding) {
            setupToolbar()
            setupChips()
            setupRecyclerView()

            viewModel.apply {
                getSavedAlbums()
                albumPaging.observe(viewLifecycleOwner) { pagingData ->
                    albumAdapter.submitData(lifecycle, pagingData)
                }
            }
            imgHide.setOnClickListener {
                hideSearchEditText()
                etSearch.setText("")
            }
        }
    }

    private fun updateChipSelection() {
        with(binding) {
            chipAlbum.isChecked = isAlbumSelected
            chipEp.isChecked = isEPSelected
            chipCompilation.isChecked = isCompilationSelected
            chipSingle.isChecked = isSingleSelected
        }
    }

    private fun FragmentAlbumHomeBinding.setupChips() {
        chipList.setOnClickListener {
            isListViewChanged = !isListViewChanged
            when {
                isListViewChanged -> chipList.setChipIconResource(R.drawable.ic_grid)
                else -> chipList.setChipIconResource(R.drawable.ic_list)
            }
            setRecyclerViewLayout(isListViewChanged)
        }

        chipSortBy.setOnClickListener { listTypeDialog() }
        chipSearch.setOnClickListener { showSearchEditText() }

        val chipClickListener: View.OnClickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.chipAlbum -> {
                    isAlbumSelected = !isAlbumSelected
                    if (isAlbumSelected) {
                        viewModel.getAlbumType(ALBUM)
                        lifecycleScope.launch {
                            dataManager.saveSelectedAlbumType(requireContext(), ALBUM)
                        }
                    } else viewModel.getSavedAlbums()
                }
                R.id.chipSingle -> {
                    isSingleSelected = !isSingleSelected
                    if (isSingleSelected) {
                        viewModel.getAlbumType(SINGLE)
                        lifecycleScope.launch {
                            dataManager.saveSelectedAlbumType(requireContext(), SINGLE)
                        }
                    } else viewModel.getSavedAlbums()
                }
                R.id.chipEp -> {
                    isEPSelected = !isEPSelected
                    if (isEPSelected) {
                        viewModel.getAlbumType(EP)
                        lifecycleScope.launch {
                            dataManager.saveSelectedAlbumType(requireContext(), EP)
                        }
                    } else viewModel.getSavedAlbums()
                }
                R.id.chipCompilation -> {
                    isCompilationSelected = !isCompilationSelected
                    if (isCompilationSelected) {
                        viewModel.getAlbumType(COMPILATION)
                        lifecycleScope.launch {
                            dataManager.saveSelectedAlbumType(requireContext(), COMPILATION)
                        }
                    } else viewModel.getSavedAlbums()
                }
            }
        }

        chipAlbum.setOnClickListener(chipClickListener)
        chipSingle.setOnClickListener(chipClickListener)
        chipEp.setOnClickListener(chipClickListener)
        chipCompilation.setOnClickListener(chipClickListener)
    }

    private fun FragmentAlbumHomeBinding.setRecyclerViewLayout(isListView: Boolean) {
        if (isListView) { ViewType.LIST } else { ViewType.GRID }
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
            adapter = albumAdapter.withLoadStateHeaderAndFooter(
                footer = LoadStateAdapter { albumAdapter.retry() },
                header = LoadStateAdapter { albumAdapter.retry() }
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
                        //viewModel.updateSortingType(ListSortingType.RECENTLY_ADDED)
                    }
                    1 -> {
                        chipSortBy.text = getString(R.string.alphabetical)
                        //viewModel.updateSortingType(ListSortingType.ALPHABETICAL)
                    }
                }
            }
            .show()
    }

    private fun FragmentAlbumHomeBinding.setupToolbar() {
        with(toolbar) {
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
                Pair(R.id.imgListenLater) {
                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumListenLaterFragment()
                    navController.navigate(action)
                    drawerLayout.close()
                },
                Pair(R.id.imgNewReleases) {
                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumNewReleaseFragment()
                    navController.navigate(action)
                    drawerLayout.close()
                }
            )
            navigationView.setNavigationItemSelectedListener { menuItem ->
                val action = drawerButtons.find { it.first == menuItem.itemId }?.second
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
        return regex.find(url)?.groupValues?.getOrNull(1)
    }

    private fun View.animationProperties(translationYValue : Float, alphaValue : Float, interpolator : Interpolator) {
        animate()
            .translationY(translationYValue)
            .alpha(alphaValue)
            .setInterpolator(interpolator)
            .start()
    }

    private fun FragmentAlbumHomeBinding.showSearchEditText() {
        if (!isSearchEditTextVisible) {
            linearLayoutSearch.apply {
                translationY = -100f
                show()
                animationProperties(0f, 1f, DecelerateInterpolator())
            }
            isSearchEditTextVisible = true
        }
    }

    private fun FragmentAlbumHomeBinding.hideSearchEditText() {
        if (isSearchEditTextVisible) {
            linearLayoutSearch.apply {
                translationY = 0f
                animationProperties(-100f, 0f, DecelerateInterpolator())
                lifecycleScope.launch {
                    delay(400)
                    hide()
                }
            }
            isSearchEditTextVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AlbumHomeFragment"
    }

    override fun onItemClick(position: Int, item: AlbumEntity) {
        val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumDetailFragment(item.id)
        navController.navigate(action)
    }
}