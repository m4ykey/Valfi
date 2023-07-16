package com.example.vuey.feature_artist.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.FragmentArtistBinding
import com.example.vuey.feature_artist.presentation.adapter.GenreAdapter
import com.example.vuey.feature_artist.presentation.adapter.TopTracksAdapter
import com.example.vuey.feature_artist.presentation.viewmodel.ArtistViewModel
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class ArtistFragment : Fragment() {

    private val artistViewModel: ArtistViewModel by viewModels()

    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!

    private val genreAdapter by lazy { GenreAdapter() }
    private val topTracksAdapter by lazy { TopTracksAdapter() }

    private val args: ArtistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            with(artistViewModel) {
                getArtistBio(args.artistName) // lastfm
                getArtistInfo(args.artistId) // spotify
                getArtistTopTracks(args.artistId) // spotify
            }
        }

        with(binding) {
            val artistSite = toolbar.menu.findItem(R.id.imgArtist)
            val saveArtist = toolbar.menu.findItem(R.id.imgSave)
            artistSite.icon.let {
                MenuItemCompat.setIconTintList(artistSite, ColorStateList.valueOf(Color.WHITE))
            }
            saveArtist.icon.let {
                MenuItemCompat.setIconTintList(saveArtist, ColorStateList.valueOf(Color.WHITE))
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            recyclerViewTopTracks.adapter = topTracksAdapter
            recyclerViewGenre.apply {
                adapter = genreAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            linearLayoutAbout.setOnClickListener {
                if (txtContentFull.visibility == View.GONE) {
                    txtContent.visibility = View.GONE
                    txtContentFull.visibility = View.VISIBLE
                } else {
                    txtContent.visibility = View.VISIBLE
                    txtContentFull.visibility = View.GONE
                }
            }
        }

        hideBottomNavigation()
        observeArtistInfo()
        observeArtistBio()
        observeArtistTopTracks()
    }

    private fun observeArtistTopTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistViewModel.artistTopTracksUiState.collect { uiState ->
                    with(binding) {
                        when {
                            uiState.isLoading -> { progressBar.visibility = View.VISIBLE }
                            uiState.isError?.isNotEmpty() == true -> {
                                showSnackbar(requireView(), "${uiState.isError}", Snackbar.LENGTH_LONG)
                            }
                            uiState.topTracksData.isNotEmpty() -> {

                                topTracksAdapter.submitTopTracks(uiState.topTracksData)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeArtistBio() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistViewModel.artistBioUiState.collect { uiState ->
                    with(binding) {
                        when {
                            uiState.isLoading -> { progressBar.visibility = View.VISIBLE }

                            uiState.isError?.isNotEmpty() == true -> {
                                showSnackbar(requireView(), "${uiState.isError}", Snackbar.LENGTH_LONG)
                            }
                            uiState.artistBioData != null -> {

                                progressBar.visibility = View.GONE

                                val artistInfo = uiState.artistBioData

                                val fulltext = artistInfo.bio.content
                                val spannableString = SpannableString(fulltext)

                                val start = fulltext.indexOf("<a")
                                val end = fulltext.indexOf("</a>") + "</a>".length

                                val clickableText = object : ClickableSpan() {
                                    override fun onClick(widget: View) {
                                        val url = "https://www.last.fm/music/${args.artistName}"
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                    }

                                    override fun updateDrawState(ds: TextPaint) {
                                        super.updateDrawState(ds)

                                        ds.isUnderlineText = true
                                        ds.color = Color.GREEN
                                    }
                                }

                                spannableString.setSpan(
                                    clickableText,
                                    start,
                                    end,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )

                                txtContent.text = artistInfo.bio.content
                                txtContentFull.apply {
                                    text = spannableString
                                    movementMethod = LinkMovementMethod.getInstance()
                                    highlightColor = Color.TRANSPARENT
                                }

                                if (artistInfo.bio.content.isEmpty()) {
                                    txtContent.visibility = View.GONE
                                    txtEmptyAbout.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    private fun observeArtistInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistViewModel.artistInfoUiState.collect { uiState ->
                    with(binding) {
                        when {
                            uiState.isLoading -> {
                                progressBar.visibility = View.VISIBLE
                            }

                            uiState.isError?.isNotEmpty() == true -> {}
                            uiState.artistInfoData != null -> {

                                progressBar.visibility = View.GONE

                                val artistInfo = uiState.artistInfoData
                                val image =
                                    artistInfo.images.find { it.height == 640 && it.width == 640 }

                                imgArtist.load(image?.url)
                                txtArtist.text = artistInfo.name
                                txtFollowers.text =
                                    "${getString(R.string.followers)} : ${artistInfo.followers.total}" + " • " + "${
                                        getString(R.string.popularity)
                                    } : ${artistInfo.popularity}"

                                if (artistInfo.genres.isEmpty()) {
                                    txtEmptyGenres.visibility = View.VISIBLE
                                } else {
                                    genreAdapter.submitGenre(artistInfo.genres)
                                }

                                toolbar.setOnMenuItemClickListener { menuItem ->
                                    when (menuItem.itemId) {
                                        R.id.imgArtist -> {
                                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(artistInfo.external_urls.spotify)))
                                            true
                                        }
                                        R.id.imgSave -> {
                                            showSnackbar(requireView(), getString(R.string.added_to_library))
                                            true
                                        }
                                        else -> { false }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}