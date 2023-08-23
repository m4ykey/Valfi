package com.example.vuey.feature_movie.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.core.common.Constants.MOVIE_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = MOVIE_TABLE_NAME)
@Parcelize
data class MovieEntity(
    @ColumnInfo(name = "movieId") @PrimaryKey(autoGenerate = false) val movieId : Int,
    @ColumnInfo(name = "movieRuntime") val movieRuntime : Int,
    @ColumnInfo(name = "movieOverview") val movieOverview : String,
    @ColumnInfo(name = "movieBackdropPath") val movieBackdropPath : String,
    @ColumnInfo(name = "moviePosterPath") val moviePosterPath : String,
    @ColumnInfo(name = "movieTitle") val movieTitle : String,
    @ColumnInfo(name = "movieReleaseDate") val movieReleaseDate : String,
    @ColumnInfo(name = "movieVoteAverage") val movieVoteAverage : Double,
    @ColumnInfo(name = "movieGenreList") val movieGenreList : List<GenreEntity>,
    @ColumnInfo(name = "movieSpokenLanguage") val movieSpokenLanguage : List<SpokenLanguageEntity>,
    @ColumnInfo(name = "movieSaveTime") val saveTime : Long = System.currentTimeMillis()
) : Parcelable {

    @Parcelize
    data class GenreEntity(
        @ColumnInfo(name = "genreName") val genreName : String
    ) : Parcelable

    @Parcelize
    data class SpokenLanguageEntity(
        @ColumnInfo(name = "spokenLanguageName") val spokenLanguageName : String
    ) : Parcelable
}
