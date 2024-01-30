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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.showToast
import com.m4ykey.ui.adapter.AlbumEntityPagingAdapter
import com.m4ykey.ui.adapter.LoadStateAdapter
import com.m4ykey.ui.databinding.FragmentAlbumHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

@AndroidEntryPoint
class AlbumHomeFragment : Fragment() {

    private var _binding : FragmentAlbumHomeBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController : NavController
    private var isListViewChanged = false
    private val albumAdapter by lazy { AlbumEntityPagingAdapter() }
    private val viewModel : AlbumViewModel by viewModels()

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
            setupRecyclerView()
            setupChips()

            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.albumPagingData.collect { pagingData ->
                    albumAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun FragmentAlbumHomeBinding.setupChips() {
        chipList.setOnClickListener {
            isListViewChanged = !isListViewChanged
            when {
                isListViewChanged -> { chipList.setChipIconResource(R.drawable.ic_grid) }
                else -> { chipList.setChipIconResource(R.drawable.ic_list) }
            }
        }
        chipSortBy.setOnClickListener { listTypeDialog() }
    }

    private fun FragmentAlbumHomeBinding.setupRecyclerView() {
        with(rvAlbums) {
            adapter = albumAdapter.withLoadStateFooter(
                footer = LoadStateAdapter()
            )

            layoutManager = GridLayoutManager(requireContext(), 3)
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

}