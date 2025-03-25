package com.m4ykey.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumStarBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumId = arguments?.getString("albumId")

        albumId?.let { id ->
            lifecycleScope.launch {
                val starsList = viewModel.getStarsById(id)

                val starsEntity = starsList.firstOrNull()
                starsEntity?.let {
                    binding.apply {
                        txtStars.text = it.stars.toString()
                        ratingBar.rating = it.stars
                    }
                }
            }
        }

        val ratingToText = mapOf(
            0.5F to "0.5",
            1F to "1",
            1.5F to "1.5",
            2F to "2",
            2.5F to "2.5",
            3F to "3",
            3.5F to "3.5",
            4F to "4",
            4.5F to "4.5",
            5F to "5"
        )

        binding.apply {
            txtStars.text = "1"
            ratingBar.rating = 1F
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                txtStars.text = ratingToText[rating] ?: ""

                val stars = StarsEntity(
                    albumId = albumId.toString(),
                    stars = rating
                )

                albumId?.let {
                    lifecycleScope.launch {
                        viewModel.insertStars(listOf(stars))
                    }
                }
            }
        }
    }
}