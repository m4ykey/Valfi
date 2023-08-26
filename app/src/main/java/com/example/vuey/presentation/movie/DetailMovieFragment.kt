package com.example.vuey.presentation.movie

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
import com.example.vuey.presentation.movie.adapter.CastAdapter
import com.example.vuey.presentation.movie.viewmodel.MovieViewModel
import com.example.vuey.presentation.movie.viewmodel.ui_state.CastMovieUiState
import com.example.vuey.presentation.movie.viewmodel.ui_state.DetailMovieUiState
import com.google.android.material.snackbar.Snackbar
import com.m4ykey.common.Constants.TMDB_IMAGE_ORIGINAL
import com.m4ykey.common.network.NetworkStateMonitor
import com.m4ykey.common.utils.formatVoteAverage
import com.m4ykey.common.utils.showSnackbar
import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
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
    private var isWatchLater = false

    private lateinit var connectivityManager: ConnectivityManager
    private val networkStateMonitor: NetworkStateMonitor by lazy {
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
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkStateMonitor.startMonitoring()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            observeMovieDetail()
            observeMovieCast()

            val movieDatabase = args.movieEntity
            val watchLaterDatabase = args.watchLaterEntity

            lifecycleScope.launch {
                viewModel.apply {
                    getMovieDetail(args.movieId)
                    getMovieCast(args.movieId)
                }

                val movie = viewModel.getMovieById(movieDatabase.movieId).first()
                val watchLater =
                    viewModel.getWatchLaterMovieById(watchLaterDatabase.movieId).first()

                isMovieSaved = movie != null
                isWatchLater = watchLater != null

                if (isMovieSaved) {
                    imgSave.setImageResource(R.drawable.ic_save)
                } else {
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                }

                if (isWatchLater) {
                    imgTime.setImageResource(R.drawable.ic_time)
                } else {
                    imgTime.setImageResource(R.drawable.ic_time_outline)
                }

                networkStateMonitor.isInternetAvailable.collect { isAvailable ->
                    if (isAvailable) {
                        recyclerViewTopCast.visibility = View.VISIBLE
                        txtEmptyCast.visibility = View.GONE
                    } else {
                        recyclerViewTopCast.visibility = View.GONE
                        txtEmptyCast.apply {
                            visibility = View.VISIBLE
                            getString(R.string.cast_is_empty)
                        }
                    }
                }
            }

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

            val watchLaterEntity = WatchLaterEntity(
                movieId = watchLaterDatabase.movieId,
                moviePosterPath = watchLaterDatabase.moviePosterPath,
                movieTitle = watchLaterDatabase.movieTitle
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
            val genreList =
                movieDatabase.movieGenreList.joinToString(separator = ", ") { it.genreName }
            val spokenLanguage =
                movieDatabase.movieSpokenLanguage.joinToString(separator = ", ") { it.spokenLanguageName }

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

            imgTime.setOnClickListener {
                isWatchLater = !isWatchLater
                if (isWatchLater) {
                    showSnackbar(
                        requireView(),
                        getString(R.string.added_to_watch_later)
                    )
                    imgTime.setImageResource(R.drawable.ic_time)
                    viewModel.insertWatchLaterMovie(watchLaterEntity)
                } else {
                    showSnackbar(
                        requireView(),
                        getString(R.string.removed_from_watch_later)
                    )
                    imgTime.setImageResource(R.drawable.ic_time_outline)
                    viewModel.deleteWatchLaterMovie(watchLaterEntity)
                }
            }

            txtVoteAverage.text = movieDatabase.movieVoteAverage.formatVoteAverage()
            txtOverview.text = movieDatabase.movieOverview
            txtOverviewFull.text = movieDatabase.movieOverview
            txtMovieTitle.text = movieDatabase.movieTitle
            txtInfo.text = "$movieRuntime • $genreList • ${
                com.m4ykey.common.utils.DateUtils.formatAirDate(movieDatabase.movieReleaseDate)
            }"
            txtSpokenLanguages.text = spokenLanguage
        }

    }

    private fun FragmentDetailMovieBinding.observeMovieDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieDetailUiState.collect { uiState ->
                    when (uiState) {
                        is DetailMovieUiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is DetailMovieUiState.Failure -> {
                            progressBar.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.message,
                                Snackbar.LENGTH_LONG
                            )
                        }

                        is DetailMovieUiState.Success -> {

                            progressBar.visibility = View.GONE

                            val movieDetail = uiState.movieData

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
                                txtSpokenLanguages.text =
                                    getString(R.string.languages_empty)
                            }

                            txtMovieTitle.text = movieDetail.title
                            val movieOverview = movieDetail.overview.ifEmpty {
                                args.movieOverview
                            }
                            txtOverview.text = movieOverview
                            txtOverviewFull.text = movieOverview

                            txtInfo.text = if (movieRuntime.isEmpty()) {
                                "$genreList • ${com.m4ykey.common.utils.DateUtils.formatAirDate(movieDetail.releaseDate)}"
                            } else if (genreList.isEmpty()) {
                                "$movieRuntime • ${com.m4ykey.common.utils.DateUtils.formatAirDate(movieDetail.releaseDate)}"
                            } else if (movieDetail.releaseDate.isEmpty()) {
                                "$movieRuntime • $genreList"
                            } else {
                                "$movieRuntime • $genreList • ${
                                    com.m4ykey.common.utils.DateUtils.formatAirDate(
                                        movieDetail.releaseDate
                                    )
                                }"
                            }

                            txtSpokenLanguages.text =
                                movieDetail.spokenLanguages.joinToString(separator = ", ") { it.name }
                            txtVoteAverage.text =
                                movieDetail.voteAverage.formatVoteAverage()

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

                            val watchLaterEntity = WatchLaterEntity(
                                movieId = movieDetail.id,
                                movieTitle = movieDetail.title,
                                moviePosterPath = movieDetail.posterPath.toString()
                            )

                            imgTime.setOnClickListener {
                                isWatchLater = !isWatchLater
                                if (isWatchLater) {
                                    showSnackbar(
                                        requireView(),
                                        getString(R.string.added_to_watch_later)
                                    )
                                    imgTime.setImageResource(R.drawable.ic_time)
                                    viewModel.insertWatchLaterMovie(watchLaterEntity)
                                } else {
                                    showSnackbar(
                                        requireView(),
                                        getString(R.string.removed_from_watch_later)
                                    )
                                    imgTime.setImageResource(R.drawable.ic_time_outline)
                                    viewModel.deleteWatchLaterMovie(watchLaterEntity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FragmentDetailMovieBinding.observeMovieCast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieCastUiState.collect { uiState ->
                    when (uiState) {
                        is CastMovieUiState.Loading -> {
                            progressBar.visibility = View.GONE
                            progressBarCast.visibility = View.VISIBLE
                        }

                        is CastMovieUiState.Failure -> {
                            progressBar.visibility = View.GONE
                            progressBarCast.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.message,
                                Snackbar.LENGTH_LONG
                            )
                        }

                        is CastMovieUiState.Success -> {
                            progressBar.visibility = View.GONE
                            progressBarCast.visibility = View.GONE
                            castAdapter.submitCast(uiState.castData)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        networkStateMonitor.stopMonitoring()
    }
}