package com.m4ykey.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.m4ykey.artist.ui.R
import com.m4ykey.artist.ui.databinding.FragmentArtistBinding
import com.m4ykey.core.Constants
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.ui.adapter.TopTrackAdapter
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

    private val topTrackAdapter by lazy { createTopTrackAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        artistId = arguments?.getString(ARG_ARTIST_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModels()
        setupRecyclerView()

        binding.toolbar.setNavigationOnClickListener { activity?.finish() }

    }

    private fun observeViewModels() {
        artistId?.let { id -> viewModel.getArtistInfo(id) }

        lifecycleScope.launch {
            viewModel.artist.collect { item -> item?.let { displayArtist(it) } }
        }

        lifecycleScope.launch {
            viewModel.topTracks.collect { tracks ->
                topTrackAdapter.submitList(tracks)
            }
        }
    }

    private fun createTopTrackAdapter() : TopTrackAdapter {
        return TopTrackAdapter()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTopTracks.apply {
            adapter = topTrackAdapter
            addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(Constants.SPACE_BETWEEN_ITEMS)))
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