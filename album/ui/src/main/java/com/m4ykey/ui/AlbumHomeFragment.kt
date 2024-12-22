package com.m4ykey.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.m4ykey.album.ui.R
import com.m4ykey.album.ui.databinding.FragmentAlbumHomeBinding
import com.m4ykey.core.Constants.ALBUM
import com.m4ykey.core.Constants.COMPILATION
import com.m4ykey.core.Constants.EP
import com.m4ykey.core.Constants.SINGLE
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.sorting.SortType
import com.m4ykey.core.views.sorting.ViewType
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.preferences.AlbumPreferences
import com.m4ykey.settings.SettingsActivity
import com.m4ykey.ui.adapter.AlbumAdapter
import com.m4ykey.ui.helpers.BooleanWrapper
import com.m4ykey.ui.helpers.createGridLayoutManager
import com.m4ykey.ui.helpers.hideSearchEditText
import com.m4ykey.ui.helpers.showSearchEditText
import com.m4ykey.ui.viewmodels.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class AlbumHomeFragment : BaseFragment<FragmentAlbumHomeBinding>(
    FragmentAlbumHomeBinding::inflate
) {

    private var isListViewChanged = false
    private val albumAdapter by lazy { createAlbumAdapter() }
    private var isSearchEditTextVisible = BooleanWrapper(false)
    private var isHidingAnimationRunning = BooleanWrapper(false)
    private var isAlbumSelected = false
    private var isEPSelected = false
    private var isSingleSelected = false
    private var isCompilationSelected = false
    @Inject
    lateinit var albumPreferences: AlbumPreferences
    private var selectedSortType : SortType = SortType.LATEST

    private val viewModel by viewModels<AlbumViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.showBottomNavigation()

        setupToolbar()
        setupChips()
        setupRecyclerView()
        handleRecyclerViewButton()

        viewModel.apply {
            lifecycleScope.launch { getSavedAlbums() }
            albumEntity.observe(viewLifecycleOwner) { albums ->
                val filteredAlbums = filterAlbums(albums)
                if (filteredAlbums.isEmpty()) {
                    albumAdapter.submitList(emptyList())
                    binding.linearLayoutEmptyList.isVisible = true
                    binding.linearLayoutEmptySearch.isVisible = false
                } else {
                    binding.linearLayoutEmptyList.isVisible = false
                    if (binding.etSearch.text.isNullOrEmpty()) {
                        albumAdapter.submitList(filteredAlbums)
                        binding.linearLayoutEmptySearch.isVisible = false
                    }
                }
            }
            binding.etSearch.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    lifecycleScope.launch { getSavedAlbums() }
                } else {
                    lifecycleScope.launch { searchAlbumByName(text.toString()) }
                }
            }
            searchResult.observe(viewLifecycleOwner) { albums ->
                val filteredAlbums = filterAlbums(albums)
                if (filteredAlbums.isEmpty()) {
                    albumAdapter.submitList(emptyList())
                    binding.linearLayoutEmptySearch.isVisible = true
                    binding.linearLayoutEmptyList.isVisible = false
                } else {
                    albumAdapter.submitList(filteredAlbums)
                    binding.linearLayoutEmptySearch.isVisible = false
                }
            }
        }
        binding.imgHide.setOnClickListener {
            hideSearchEditText(
                coroutineScope = lifecycleScope,
                linearLayout = binding.linearLayoutSearch,
                isSearchEditTextVisible = isSearchEditTextVisible,
                isHidingAnimationRunning = isHidingAnimationRunning,
                translationYValue = -100f
            )
            binding.etSearch.setText(getString(R.string.empty_string))
        }
    }

    private fun filterAlbums(albums : List<AlbumEntity>) : List<AlbumEntity> {
        return if (
            !isAlbumSelected && !isEPSelected && !isCompilationSelected && !isSingleSelected
        ) {
            albums
        } else {
            albums.filter { album ->
                when {
                    isAlbumSelected && album.albumType == ALBUM -> true
                    isEPSelected && album.albumType == EP -> true
                    isCompilationSelected && album.albumType == COMPILATION -> true
                    isSingleSelected && album.albumType == SINGLE -> true
                    else -> false
                }
            }
        }
    }

    private fun updateChipSelection() {
        binding.apply {
            chipAlbum.isChecked = isAlbumSelected
            chipEp.isChecked = isEPSelected
            chipCompilation.isChecked = isCompilationSelected
            chipSingle.isChecked = isSingleSelected
        }
    }

    private fun handleRecyclerViewButton() {
        binding.apply {
            rvAlbums.addOnScrollListener(scrollListener(btnToTop))
            btnToTop.setOnClickListener {
                rvAlbums.smoothScrollToPosition(0)
            }
        }
    }

    private fun createAlbumAdapter() : AlbumAdapter {
        return AlbumAdapter(
            onAlbumClick = { album ->
                val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumDetailFragment(album.id)
                findNavController().navigate(action)
            }
        )
    }

    private fun setupChips() {
        binding.apply {
            chipList.setOnClickListener {
                isListViewChanged = !isListViewChanged
                lifecycleScope.launch {
                    if (isListViewChanged) {
                        albumPreferences.saveSelectedViewType(ViewType.LIST)
                        chipList.setChipIconResource(R.drawable.ic_grid)
                    } else {
                        albumPreferences.deleteSelectedViewType()
                        chipList.setChipIconResource(R.drawable.ic_list)
                    }
                    setRecyclerViewLayout(isListViewChanged)
                }
            }

            chipSortBy.setOnClickListener { sortTypeDialog() }
            chipSearch.setOnClickListener {
                isSearchEditTextVisible = showSearchEditText(isSearchEditTextVisible, linearLayoutSearch, -100f)
            }

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
            lifecycleScope.launch {
                viewModel.getAlbumType(albumType)
                albumPreferences.saveSelectedAlbumType(albumType)
            }
        } else {
            lifecycleScope.launch {
                viewModel.getSavedAlbums()
                albumPreferences.deleteSelectedAlbumType()
            }
        }
    }

    private fun setRecyclerViewLayout(isListView: Boolean) {
        val viewType = if (isListView) ViewType.LIST else ViewType.GRID
        binding.rvAlbums.apply {
            layoutManager = if (isListView) {
                LinearLayoutManager(requireContext())
            } else {
                createGridLayoutManager(requireContext())
            }
            albumAdapter.viewType = viewType
        }
    }

    private fun setupRecyclerView() {
        binding.rvAlbums.apply {
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))

            layoutManager = if (albumAdapter.viewType == ViewType.LIST) {
                LinearLayoutManager(requireContext())
            } else {
                createGridLayoutManager(requireContext())
            }
            adapter = albumAdapter
        }
    }

    private fun sortTypeDialog() {
        val sortOptions = resources.getStringArray(R.array.sort_options)
        MaterialAlertDialogBuilder(requireContext(), R.style.SortMaterialAlertDialog)
            .setTitle(R.string.sort_by)
            .setItems(sortOptions) { _, index ->
                val listType = when (index) {
                    0 -> SortType.LATEST
                    1 -> SortType.OLDEST
                    2 -> SortType.ALPHABETICAL
                    else -> throw IllegalArgumentException(getString(com.m4ykey.core.R.string.invalid_sort_index))
                }
                updateListWithSortType(listType)
                saveSelectedSortType(listType)
            }
            .show()
    }

    private fun saveSelectedSortType(sortType: SortType) {
        lifecycleScope.launch {
            if (sortType == SortType.LATEST) {
                albumPreferences.deleteSelectedSortType()
            } else {
                albumPreferences.saveSelectedSortType(sortType)
            }
        }
    }

    private fun updateListWithSortType(sortType: SortType) {
        binding.apply {
            lifecycleScope.launch {
                when (sortType) {
                    SortType.LATEST -> {
                        chipSortBy.text = getString(R.string.latest)
                        viewModel.getSavedAlbums()
                    }

                    SortType.OLDEST -> {
                        chipSortBy.text = getString(R.string.oldest)
                        viewModel.getSavedAlbumAsc()
                    }

                    SortType.ALPHABETICAL -> {
                        chipSortBy.text = getString(R.string.alphabetical)
                        viewModel.getAlbumSortedByName()
                    }
                }
                selectedSortType = sortType
            }
        }
    }

    private fun setupToolbar() {
        binding.apply {
            val buttons = listOf(
                Pair(R.id.imgSearch) {
                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumSearchFragment()
                    findNavController().navigate(action)
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
                Pair(R.id.imgSettings) {
                    val intent = Intent(requireContext(), SettingsActivity::class.java)
                    startActivity(intent)
                    drawerLayout.close()
                },
                Pair(R.id.imgListenLater) {
                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumListenLaterFragment()
                    findNavController().navigate(action)
                    drawerLayout.close()
                },
                Pair(R.id.imgNewReleases) {
                    val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumNewReleaseFragment()
                    findNavController().navigate(action)
                    drawerLayout.close()
                }
            )
            navigationView.setNavigationItemSelectedListener { menuItem ->
                val action = drawerButtons.find { it.first == menuItem.itemId }?.second
                action?.invoke()
                true
            }

            toolbar.setNavigationOnClickListener { drawerLayout.open() }

            val currentTime = LocalTime.now()
            val greeting = when {
                currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(18,0)) -> getString(R.string.hello)
                else -> getString(R.string.good_evening)
            }

            val headerView = navigationView.getHeaderView(0)
            val headerViewTextView = headerView.findViewById<TextView>(R.id.txtGreeting)
            headerViewTextView.text = greeting
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
                    findNavController().navigate(action)
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

    private fun resetSearchState() {
        binding.apply {
            if (etSearch.text.isNullOrBlank() && !isSearchEditTextVisible.value) {
                linearLayoutSearch.isVisible = false
                etSearch.setText(getString(R.string.empty_string))
            } else {
                linearLayoutSearch.isVisible = true
            }
        }
    }

    private fun readSelectedSortType() {
        lifecycleScope.launch {
            albumPreferences.getSelectedSortType().collect { savedSortType ->
                if (savedSortType != null) {
                    selectedSortType = savedSortType
                    updateListWithSortType(savedSortType)
                }
            }
        }
    }

    private fun readSelectedViewType() {
        lifecycleScope.launch {
            albumPreferences.getSelectedViewType().collect { selectedViewType ->
                binding.apply {
                    if (selectedViewType != null) {
                        albumAdapter.viewType = selectedViewType
                        chipList.setChipIconResource(if (selectedViewType == ViewType.LIST) R.drawable.ic_grid else R.drawable.ic_list)
                        isListViewChanged = selectedViewType == ViewType.LIST
                    } else {
                        albumAdapter.viewType = ViewType.GRID
                        chipList.setChipIconResource(R.drawable.ic_list)
                    }

                    val layoutManager = when (albumAdapter.viewType) {
                        ViewType.LIST -> LinearLayoutManager(requireContext())
                        ViewType.GRID -> createGridLayoutManager(requireContext())
                    }
                    rvAlbums.layoutManager = layoutManager
                }
            }
        }
    }

    private fun readSelectedAlbumType() {
        lifecycleScope.launch {
            albumPreferences.getSelectedAlbumType().collect { selectedAlbum ->
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
        }
    }

    override fun onStart() {
        super.onStart()
        readSelectedAlbumType()
        readSelectedSortType()
        readSelectedViewType()
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
    }

    companion object {
        const val TAG = "AlbumHomeFragment"
    }
}