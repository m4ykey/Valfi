package com.m4ykey.valfi2.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.m4ykey.valfi2.R
import com.m4ykey.valfi2.databinding.FragmentAlbumHomeBinding

class AlbumHomeFragment : Fragment() {

    private var _binding : FragmentAlbumHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            linearLayoutSort.setOnClickListener {
                openMaterialDialog()
            }
        }
    }

    private fun FragmentAlbumHomeBinding.openMaterialDialog() {
        val sortOptions = resources.getStringArray(R.array.sort_options)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.sort_by)
            .setItems(sortOptions) { _, index ->
                when (index) {
                    0 -> {
                        txtSort.setText(R.string.recently_added)
                    }
                    1 -> {
                        txtSort.setText(R.string.alphabetical)
                    }
                }
            }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}