package com.m4ykey.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.m4ykey.album.ui.databinding.FragmentAlbumStarBottomSheetBinding
import com.m4ykey.data.local.model.StarsEntity
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumStarBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentAlbumStarBottomSheetBinding? = null
    val binding get() = _binding!!

    private val viewModel by viewModels<AlbumViewModel>()

    private var albumId : String? = null
    private var existingStars : StarsEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumStarBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRatingTranslations()
        albumId = arguments?.getString("albumId")
        loadExistingRating()
        setupDeleteButton()
    }

    private fun setupRatingTranslations() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            val ratingToText = when (rating) {
                0.5F -> "0.5"
                1F -> "1"
                1.5F -> "1.5"
                2F -> "2"
                2.5F -> "2.5"
                3F -> "3"
                3.5F -> "3.5"
                4F -> "4"
                4.5F -> "4.5"
                5F -> "5"
                else -> "0"
            }

            binding.txtStars.text = ratingToText

            if (fromUser) {
                saveRating(rating)
            }
        }
    }

    private fun loadExistingRating() {
        albumId?.let { id ->
            lifecycleScope.launch {
                val starsList = viewModel.getStarsById(id)

                if (starsList.isNotEmpty()) {
                    existingStars = starsList.first()
                    binding.apply {
                        txtStars.text = existingStars?.stars?.toString() ?: "0"
                        ratingBar.rating = existingStars?.stars ?: 0F

                        btnDelete.isVisible = true
                    }
                } else {
                    binding.apply {
                        txtStars.text = "0"
                        ratingBar.rating = 0F
                        btnDelete.isVisible = false
                    }
                }
            }
        }
    }

    private fun setupDeleteButton() {
        binding.btnDelete.setOnClickListener {
            albumId?.let { id ->
                lifecycleScope.launch {
                    viewModel.deleteStars(id)
                    dismiss()
                }
            }
        }
    }

    private fun saveRating(rating : Float) {
        albumId?.let { id ->
            val stars = StarsEntity(
                albumId = id,
                stars = rating
            )

            lifecycleScope.launch {
                viewModel.insertStars(listOf(stars))
                binding.btnDelete.isVisible = false
            }
        }
    }
}