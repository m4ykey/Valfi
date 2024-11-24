package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.m4ykey.artist.ui.R
import com.m4ykey.artist.ui.databinding.FragmentArtistBinding
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.loadImage
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.ui.helpers.getLargestImageUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat

@AndroidEntryPoint
class ArtistFragment : BaseFragment<FragmentArtistBinding>(
    FragmentArtistBinding::inflate
) {

    companion object {
        private const val ARG_ARTIST_ID = "artistId"

        fun newInstance(artistId : String?) : ArtistFragment {
            val fragment = ArtistFragment()
            val args = Bundle().apply {
                putString(ARG_ARTIST_ID, artistId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var artistId : String? = null

    private val viewModel by viewModels<ArtistViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        artistId = arguments?.getString(ARG_ARTIST_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModels()

        binding.toolbar.setNavigationOnClickListener { activity?.finish() }

    }

    private fun observeViewModels() {
        artistId?.let { viewModel.getArtist(it) }

        lifecycleScope.launch {
            viewModel.artist.collect { item -> item?.let { displayArtist(it) } }
        }
    }

    private fun displayArtist(item : Artist) {
        binding.apply {
            val formattedFollowers = NumberFormat.getInstance().format(item.followers.total)

            loadImage(imgArtist, item.getLargestImageUrl().toString(), requireContext())
            txtArtistName.text = item.name
            txtFollowers.text = getString(R.string.followers_count, formattedFollowers, getString(R.string.followers))
        }
    }
}