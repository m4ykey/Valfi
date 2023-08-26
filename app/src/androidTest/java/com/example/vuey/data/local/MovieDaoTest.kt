package com.example.vuey.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.m4ykey.local.movie.dao.MovieDao
import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.local.database.VueyDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database : VueyDatabase
    private lateinit var dao : MovieDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VueyDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.movieDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertMovie() = runTest {
        val movieGenre = (1..5).map { name ->
            MovieEntity.GenreEntity(
                genreName = "genre $name"
            )
        }
        val movieSpokenLanguage = (1..5).map { language ->
            MovieEntity.SpokenLanguageEntity(
                spokenLanguageName = "spoken $language"
            )
        }
        val movieEntity = MovieEntity(
            movieBackdropPath = "backdropPath.jpg",
            movieId = 1,
            movieVoteAverage = 5.3,
            movieOverview = "Movie Overview",
            moviePosterPath = "posterPath.jpg",
            movieReleaseDate = "2022",
            movieTitle = "Movie Title",
            movieRuntime = 90,
            movieGenreList = movieGenre,
            movieSpokenLanguage = movieSpokenLanguage
        )
        dao.insertMovie(movieEntity)

        val allMovies = dao.getAllMovies().first()

        assertThat(allMovies).contains(movieEntity)
    }

    @Test
    fun deleteMovie() = runTest {
        val movieGenre = (1..5).map { name ->
            MovieEntity.GenreEntity(
                genreName = "genre $name"
            )
        }
        val movieSpokenLanguage = (1..5).map { language ->
            MovieEntity.SpokenLanguageEntity(
                spokenLanguageName = "spoken $language"
            )
        }
        val movieEntity = MovieEntity(
            movieBackdropPath = "backdropPath.jpg",
            movieId = 1,
            movieVoteAverage = 5.3,
            movieOverview = "Movie Overview",
            moviePosterPath = "posterPath.jpg",
            movieReleaseDate = "2022",
            movieTitle = "Movie Title",
            movieRuntime = 90,
            movieGenreList = movieGenre,
            movieSpokenLanguage = movieSpokenLanguage
        )
        dao.insertMovie(movieEntity)
        dao.deleteMovie(movieEntity)

        val allMovies = dao.getAllMovies().first()

        assertThat(allMovies).contains(movieEntity)
    }

}