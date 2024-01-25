package com.m4ykey.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.isNightMode
import com.m4ykey.ui.databinding.FragmentAlbumHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumHomeFragment : Fragment() {

    private var _binding : FragmentAlbumHomeBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController : NavController

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
            chipSort.setOnClickListener { openSortDialog() }
            chipAlbumType.setOnClickListener { openAlbumTypeDialog() }
        }
    }

    private fun FragmentAlbumHomeBinding.openAlbumTypeDialog() {
        val sortOptions = resources.getStringArray(R.array.album_options)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.sort_by)
            .setItems(sortOptions) { _, index ->
                when (index) {
                    0 -> { chipAlbumType.text = getString(R.string.album) }
                    1 -> { chipAlbumType.text = getString(R.string.single) }
                    2 -> { chipAlbumType.text = getString(R.string.ep) }
                }
            }.show()
    }

    private fun FragmentAlbumHomeBinding.openSortDialog() {
        val sortOptions = resources.getStringArray(R.array.sort_options)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.sort_by)
            .setItems(sortOptions) { _, index ->
                when (index) {
                    0 -> { chipSort.text = getString(R.string.alphabetical) }
                    1 -> { chipSort.text = getString(R.string.recently_added) }
                }
            }.show()
    }

    private fun FragmentAlbumHomeBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = if (isNightMode) {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        }


        with(toolbar) {
            menu.findItem(R.id.imgSearch).setIconTintList(iconTint)
            menu.findItem(R.id.imgLink).setIconTintList(iconTint)
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
                                val albumId = getAlbumIdFromUrl(albumUrl)
                                val action = AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumDetailFragment(albumId ?: "")
                                navController.navigate(action)
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

    private fun getAlbumIdFromUrl(url: String): String? {
        val regex = Regex("/album/([^/?]+)")
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.getOrNull(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}