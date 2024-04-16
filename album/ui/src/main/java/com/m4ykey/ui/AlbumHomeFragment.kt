package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
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
import com.m4ykey.core.views.sorting.ViewType
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.adapter.AlbumPagingAdapter
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.databinding.FragmentAlbumHomeBinding
import com.m4ykey.ui.helpers.animationPropertiesY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import javax.inject.Inject

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
    private var isHidingAnimationRunning = false
    private var isAlbumSelected = false
    private var isEPSelected = false
    private var isSingleSelected = false
    private var isCompilationSelected = false
    @Inject
    lateinit var dataManager: DataManager

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

        lifecycleScope.launch {
            val selectedViewType = dataManager.getSelectedViewType(requireContext())

            if (selectedViewType != null) {
                albumAdapter.viewType = selectedViewType
                binding.chipList.setChipIconResource(if (selectedViewType == ViewType.LIST) R.drawable.ic_grid else R.drawable.ic_list)
                isListViewChanged = selectedViewType == ViewType.LIST
            } else {
                val defaultViewType = ViewType.GRID
                albumAdapter.viewType = defaultViewType
                binding.chipList.setChipIconResource(R.drawable.ic_list)
            }

            val layoutManager = when (albumAdapter.viewType) {
                ViewType.LIST -> LinearLayoutManager(requireContext())
                ViewType.GRID -> GridLayoutManager(requireContext(), 3)
            }
            binding.rvAlbums.layoutManager = layoutManager

            val selectedAlbum = dataManager.getSelectedAlbumType(requireContext())
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
                etSearch.doOnTextChanged { text, _, _, _ -> searchAlbumByName(text.toString()) }
                searchResult.observe(viewLifecycleOwner) { pagingData ->
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

    private fun setupChips() {
        with(binding) {
            chipList.setOnClickListener {
                isListViewChanged = !isListViewChanged
                lifecycleScope.launch {
                    if (isListViewChanged) {
                        dataManager.saveSelectedViewType(requireContext(), ViewType.LIST)
                        chipList.setChipIconResource(R.drawable.ic_grid)
                    } else {
                        dataManager.clearSelectedViewType(requireContext())
                        chipList.setChipIconResource(R.drawable.ic_list)
                    }
                    setRecyclerViewLayout(isListViewChanged)
                }
            }

            chipSortBy.setOnClickListener { listTypeDialog() }
            chipSearch.setOnClickListener { showSearchEditText() }

            val chipClickListener : View.OnClickListener = View.OnClickListener { view ->
                when (view.id) {
                    R.id.chipAlbum -> {
                        isAlbumSelected = !isAlbumSelected
                        handleChipClick(isAlbumSelected, ALBUM)
                    }
                    R.id.chipEp -> {
                        isEPSelected = !isEPSelected
                        handleChipClick(isEPSelected, EP)
                    }
                    R.id.chipSingle -> {
                        isSingleSelected = !isSingleSelected
                        handleChipClick(isSingleSelected, SINGLE)
                    }
                    R.id.chipCompilation -> {
                        isCompilationSelected = !isCompilationSelected
                        handleChipClick(isCompilationSelected, COMPILATION)
                    }
                }
            }

            chipAlbum.setOnClickListener(chipClickListener)
            chipSingle.setOnClickListener(chipClickListener)
            chipEp.setOnClickListener(chipClickListener)
            chipCompilation.setOnClickListener(chipClickListener)
        }
    }

    private fun handleChipClick(isSelected : Boolean, albumType : String) {
        if (isSelected) {
            viewModel.getAlbumType(albumType)
            lifecycleScope.launch { dataManager.saveSelectedAlbumType(requireContext(), albumType) }
        } else {
            viewModel.getSavedAlbums()
            lifecycleScope.launch { dataManager.clearSelectedAlbumType(requireContext()) }
        }
    }

    private fun setRecyclerViewLayout(isListView: Boolean) {
        val viewType = if (isListView) ViewType.LIST else ViewType.GRID
        binding.rvAlbums.layoutManager = if (isListView) {
            LinearLayoutManager(requireContext())
        } else {
            GridLayoutManager(requireContext(), 3)
        }
        albumAdapter.viewType = viewType
    }

    private fun setupRecyclerView() {
        with(binding.rvAlbums) {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            val layoutManager = if (albumAdapter.viewType == ViewType.LIST) {
                LinearLayoutManager(requireContext())
            } else {
                GridLayoutManager(requireContext(), 3)
            }
            this.layoutManager = layoutManager

            adapter = albumAdapter.withLoadStateHeaderAndFooter(
                footer = LoadStateAdapter { albumAdapter.retry() },
                header = LoadStateAdapter { albumAdapter.retry() }
            )
        }
    }

    private fun listTypeDialog() {
        with(binding) {
            val sortOptions = resources.getStringArray(R.array.sort_options)

            MaterialAlertDialogBuilder(requireContext(), R.style.SortMaterialAlertDialog)
                .setTitle(R.string.sort_by)
                .setItems(sortOptions) { _, index ->
                    when (index) {
                        0 -> {
                            chipSortBy.text = getString(R.string.recently_added)
                        }
                        1 -> {
                            chipSortBy.text = getString(R.string.alphabetical)
                        }
                    }
                }
                .show()
        }
    }

    private fun setupToolbar() {
        with(binding) {
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
                toolbar.menu.findItem(itemId)?.setOnMenuItemClickListener {
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

            toolbar.setNavigationOnClickListener { drawerLayout.open() }
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

    private fun showSearchEditText() {
        if (!isSearchEditTextVisible) {
            binding.linearLayoutSearch.apply {
                translationY = -100f
                show()
                animationPropertiesY(0f, 1f, DecelerateInterpolator())
            }
            isSearchEditTextVisible = true
        }
    }

    private fun hideSearchEditText() {
        if (isSearchEditTextVisible && !isHidingAnimationRunning) {
            isHidingAnimationRunning = true
            binding.linearLayoutSearch.apply {
                translationY = 0f
                animationPropertiesY(-100f, 0f, DecelerateInterpolator())
            }
            lifecycleScope.launch {
                delay(400)
                binding.linearLayoutSearch.hide()
                isSearchEditTextVisible = false
                isHidingAnimationRunning = false
            }
        }
    }

    private fun resetSearchState() {
        with(binding) {
            if (etSearch.text.isNullOrBlank() && !isSearchEditTextVisible) {
                linearLayoutSearch.isVisible = false
                etSearch.setText("")
            } else {
                linearLayoutSearch.isVisible = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
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