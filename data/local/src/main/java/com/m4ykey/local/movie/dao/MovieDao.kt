package com.m4ykey.local.movie.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchLaterMovie(watchLaterEntity: WatchLaterEntity)

    @Delete
    suspend fun deleteWatchLaterMovie(watchLaterEntity: WatchLaterEntity)

    @Query("SELECT * FROM watch_later_table ORDER BY saveTime ASC")
    fun getAllWatchLaterMovies() : Flow<List<WatchLaterEntity>>

    @Query("SELECT * FROM watch_later_table WHERE movieId = :movieId")
    fun getWatchLaterMovieById(movieId: Int) : Flow<WatchLaterEntity>

    @Query("SELECT COUNT(*) FROM watch_later_table")
    fun getWatchLaterMovieCount() : Flow<Int>

}