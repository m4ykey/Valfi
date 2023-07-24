package com.example.vuey.feature_movie.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.vuey.databinding.FragmentDetailMovieBinding
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.presentation.adapter.CastAdapter
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.example.vuey.core.common.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.core.common.network.NetworkStateMonitor
import com.example.vuey.core.common.utils.DateUtils
import com.example.vuey.core.common.utils.formatVoteAverage
import com.example.vuey.core.common.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val args: DetailMovieFragmentArgs by navArgs()

    private val viewModel: MovieViewModel by viewModels()

    private val castAdapter by lazy { CastAdapter() }

    private var isMovieSaved = false

    private lateinit var connectivityManager : ConnectivityManager
    private val networkStateMonitor : NetworkStateMonitor by lazy {
        NetworkStateMonitor(connectivityManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkStateMonitor.startMonitoring()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMovieDetail()
        observeMovieCast()
        hideBottomNavigation()

        lifecycleScope.launch {
            viewModel.getMovieDetail(args.movie.id)
            viewModel.getMovieCast(args.movie.id)

            binding.swipeRefresh.apply {
                setOnRefreshListener {
                    launch {
                        viewModel.refreshDetail(args.movie.id)
                        isRefreshing = false
                    }
                }
            }

            networkStateMonitor.isInternetAvailable.collect { isAvailable ->
                if (isAvailable) {
                    binding.recyclerViewTopCast.visibility = View.VISIBLE
                    binding.txtEmptyCast.visibility = View.GONE
                } else {
                    binding.recyclerViewTopCast.visibility = View.GONE
                    binding.txtEmptyCast.apply {
                        visibility = View.VISIBLE
                        getString(R.string.cast_is_empty)
                    }
                }
            }
        }

        val movieDatabase = args.movieEntity

        val movieEntity = MovieEntity(
            movieBackdropPath = movieDatabase.movieBackdropPath,
            movieGenreList = movieDatabase.movieGenreList,
            movieId = movieDatabase.movieId,
            movieOverview = movieDatabase.movieOverview,
            moviePosterPath = movieDatabase.moviePosterPath,
            movieReleaseDate = movieDatabase.movieReleaseDate,
            movieRuntime = movieDatabase.movieRuntime,
            movieSpokenLanguage = movieDatabase.movieSpokenLanguage,
            movieTitle = movieDatabase.movieTitle,
            movieVoteAverage = movieDatabase.movieVoteAverage
        )

        val movieHour = movieDatabase.movieRuntime / 60
        val movieMinute = movieDatabase.movieRuntime % 60
        val movieRuntime = if (movieHour == 0) {
            String.format("%d min", movieMinute)
        } else {
            String.format(
                "%d ${getString(R.string.hour)} %d min",
                movieHour,
                movieMinute
            )
        }
        val genreList = movieDatabase.movieGenreList.joinToString(separator = ", ") { it.genreName }
        val spokenLanguage =
            movieDatabase.movieSpokenLanguage.joinToString(separator = ", ") { it.spokenLanguageName }

        viewModel.getMovieById(args.movieEntity.movieId).onEach { movie ->
            isMovieSaved = if (movie == null) {
                binding.imgSave.setImageResource(R.drawable.ic_save_outlined)
                false
            } else {
                binding.imgSave.setImageResource(R.drawable.ic_save)
                true
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        with(binding) {

            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            recyclerViewTopCast.apply {
                adapter = castAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            linearLayoutOverview.setOnClickListener {
                if (txtOverviewFull.visibility == View.GONE) {
                    txtOverview.visibility = View.GONE
                    txtOverviewFull.visibility = View.VISIBLE
                } else {
                    txtOverview.visibility = View.VISIBLE
                    txtOverviewFull.visibility = View.GONE
                }
            }

            if (movieDatabase.movieSpokenLanguage.isEmpty()) {
                txtSpokenLanguages.text = getString(R.string.languages_empty)
            }

            imgBackdrop.load(TMDB_IMAGE_ORIGINAL + movieDatabase.movieBackdropPath) {
                crossfade(true)
                crossfade(500)
            }

            imgSave.setOnClickListener {
                isMovieSaved = !isMovieSaved
                if (isMovieSaved) {
                    showSnackbar(requireView(), getString(R.string.added_to_library))
                    imgSave.setImageResource(R.drawable.ic_save)
                    viewModel.insertMovie(movieEntity)
                } else {
                    showSnackbar(requireView(), getString(R.string.removed_from_library))
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                    viewModel.deleteMovie(movieEntity)
                }
            }

            txtVoteAverage.text = movieDatabase.movieVoteAverage.formatVoteAverage()
            txtOverview.text = movieDatabase.movieOverview
            txtOverviewFull.text = movieDatabase.movieOverview
            txtMovieTitle.text = movieDatabase.movieTitle
            txtInfo.text = "$movieRuntime • $genreList • ${
                DateUtils.formatAirDate(movieDatabase.movieReleaseDate)
            }"
            txtSpokenLanguages.text = spokenLanguage
        }

    }

    private fun observeMovieDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieDetailUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        uiState.isError?.isNotEmpty() == true -> {
                            binding.progressBar.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.isError.toString(),
                                Snackbar.LENGTH_LONG
                            )
                        }

                        uiState.detailMovieData != null -> {
                            binding.progressBar.visibility = View.GONE

                            val movieDetail = uiState.detailMovieData

                            val genreList =
                                movieDetail.genreList.joinToString(separator = ", ") { it.name }
                            val movieHour = movieDetail.runtime / 60
                            val movieMinute = movieDetail.runtime % 60
                            val movieRuntime = if (movieHour == 0) {
                                String.format("%d min", movieMinute)
                            } else {
                                String.format(
                                    "%d ${getString(R.string.hour)} %d min",
                                    movieHour,
                                    movieMinute
                                )
                            }

                            with(binding) {

                                if (!movieDetail.backdropPath.isNullOrEmpty()) {
                                    imgBackdrop.load(TMDB_IMAGE_ORIGINAL + movieDetail.backdropPath) {
                                        crossfade(true)
                                        crossfade(500)
                                    }
                                } else {
                                    imgBackdrop.load(TMDB_IMAGE_ORIGINAL + movieDetail.posterPath) {
                                        crossfade(true)
                                        crossfade(500)
                                    }
                                }

                                if (movieDetail.spokenLanguages.isEmpty()) {
                                    txtSpokenLanguages.text = getString(R.string.languages_empty)
                                }

                                txtMovieTitle.text = movieDetail.title
                                val movieOverview = movieDetail.overview.ifEmpty {
                                    args.movie.overview
                                }
                                txtOverview.text = movieOverview
                                txtOverviewFull.text = movieOverview

                                txtInfo.text = if (movieRuntime.isEmpty()) {
                                    "$genreList • ${DateUtils.formatAirDate(movieDetail.releaseDate)}"
                                } else if (genreList.isEmpty()) {
                                    "$movieRuntime • ${DateUtils.formatAirDate(movieDetail.releaseDate)}"
                                } else if (movieDetail.releaseDate.isEmpty()) {
                                    "$movieRuntime • $genreList"
                                } else {
                                    "$movieRuntime • $genreList • ${DateUtils.formatAirDate(movieDetail.releaseDate)}"
                                }

                                txtSpokenLanguages.text =
                                    movieDetail.spokenLanguages.joinToString(separator = ", ") { it.name }
                                txtVoteAverage.text = movieDetail.voteAverage.formatVoteAverage()

                                val movieEntity = MovieEntity(
                                    movieBackdropPath = movieDetail.backdropPath.toString(),
                                    movieId = movieDetail.id,
                                    movieOverview = movieOverview,
                                    moviePosterPath = movieDetail.posterPath.toString(),
                                    movieReleaseDate = movieDetail.releaseDate,
                                    movieRuntime = movieDetail.runtime,
                                    movieTitle = movieDetail.title,
                                    movieVoteAverage = movieDetail.voteAverage,
                                    movieGenreList = movieDetail.genreList.map { genre ->
                                        MovieEntity.GenreEntity(
                                            genreName = genre.name
                                        )
                                    },
                                    movieSpokenLanguage = movieDetail.spokenLanguages.map { language ->
                                        MovieEntity.SpokenLanguageEntity(
                                            spokenLanguageName = language.name
                                        )
                                    }
                                )

                                imgSave.setOnClickListener {
                                    isMovieSaved = !isMovieSaved
                                    if (isMovieSaved) {
                                        showSnackbar(
                                            requireView(),
                                            getString(R.string.added_to_library)
                                        )
                                        imgSave.setImageResource(R.drawable.ic_save)
                                        viewModel.insertMovie(movieEntity)
                                    } else {
                                        showSnackbar(
                                            requireView(),
                                            getString(R.string.removed_from_library)
                                        )
                                        imgSave.setImageResource(R.drawable.ic_save_outlined)
                                        viewModel.deleteMovie(movieEntity)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeMovieCast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieCastUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                            binding.progressBar.visibility = View.GONE
                            binding.progressBarCast.visibility = View.VISIBLE
                        }

                        uiState.isError?.isNotEmpty() == true -> {
                            binding.progressBar.visibility = View.GONE
                            binding.progressBarCast.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.isError.toString(),
                                Snackbar.LENGTH_LONG
                            )
                        }

                        uiState.castMovieData.isNotEmpty() -> {
                            binding.progressBar.visibility = View.GONE
                            binding.progressBarCast.visibility = View.GONE
                            castAdapter.submitCast(uiState.castMovieData)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        networkStateMonitor.stopMonitoring()
    }
}