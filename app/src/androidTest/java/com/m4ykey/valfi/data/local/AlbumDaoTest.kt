package com.m4ykey.valfi.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.m4ykey.local.album.dao.AlbumDao
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.database.VueyDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AlbumDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database : VueyDatabase
    private lateinit var dao : AlbumDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VueyDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.albumDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAlbum() = runTest {
        val trackList = (1..10).map { index ->
            AlbumEntity.TrackListEntity(
                durationMs = index * 1000,
                trackName = "random title $index"
            )
        }
        val artistList = (1..5).map { index ->
            AlbumEntity.ArtistEntity(
                id = index.toString(),
                name = "artist $index",
                externalUrls = AlbumEntity.ExternalUrlsEntity(
                    spotify = "random $index link"
                )
            )
        }
        val albumItem = AlbumEntity(
            albumName = "MM...FOOD",
            albumType = "Album",
            id = "albumId",
            albumLength = 23,
            releaseDate = "12 April 1999",
            totalTracks = 21,
            externalUrls = AlbumEntity.ExternalUrlsEntity(
                spotify = "random link"
            ),
            trackList = trackList,
            artistList = artistList,
            albumCover = AlbumEntity.ImageEntity(
                height = 640,
                width = 640,
                url = "album cover link"
            )
        )
        dao.insertAlbum(albumItem)

        val allAlbums = dao.getAllAlbums().first()

        assertThat(allAlbums).contains(albumItem)
    }

    @Test
    fun deleteAlbum() = runTest {
        val trackList = (1..10).map { index ->
            AlbumEntity.TrackListEntity(
                durationMs = index * 1000,
                trackName = "random title $index"
            )
        }
        val artistList = (1..5).map { index ->
            AlbumEntity.ArtistEntity(
                id = index.toString(),
                name = "artist $index",
                externalUrls = AlbumEntity.ExternalUrlsEntity(
                    spotify = "random $index link"
                )
            )
        }
        val albumItem = AlbumEntity(
            albumName = "MM...FOOD",
            albumType = "Album",
            id = "albumId",
            albumLength = 23,
            releaseDate = "12 April 1999",
            totalTracks = 21,
            externalUrls = AlbumEntity.ExternalUrlsEntity(
                spotify = "random link"
            ),
            trackList = trackList,
            artistList = artistList,
            albumCover = AlbumEntity.ImageEntity(
                height = 640,
                width = 640,
                url = "album cover link"
            )
        )
        dao.insertAlbum(albumItem)
        dao.deleteAlbum(albumItem)

        val allAlbums = dao.getAllAlbums().first()

        assertThat(allAlbums).doesNotContain(albumItem)
    }

}