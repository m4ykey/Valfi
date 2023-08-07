package com.example.vuey.feature_movie.data.local.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table ORDER BY movieSaveTime ASC")
    fun getAllMovies() : Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_table WHERE movieId = :movieId")
    fun getMovieById(movieId : Int) : Flow<MovieEntity>

    @Query("SELECT SUM(movieRuntime) FROM movie_table")
    fun getTotalLength(): Flow<Int>

    @Query("SELECT COUNT(*) FROM movie_table")
    fun getMovieCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

}