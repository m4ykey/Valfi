package com.m4ykey.settings.data.file

import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.CopyrightEntity
import com.m4ykey.data.local.model.TrackEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class JsonFileSaverTest {

    private lateinit var repository : AlbumRepository
    private lateinit var trackRepository: TrackRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        trackRepository = mockk(relaxed = true)
    }

    @Test
    fun `generateJsonData should return correct JSON`() = runTest {
        val artists = ArtistEntity(
            name = "artist",
            url = "artistUrl",
            albumId = "albumId",
            artistId = "artistId"
        )

        val copyrights = CopyrightEntity(
            text = "copyright"
        )

        val albumItem = AlbumEntity(
            totalTracks = 12,
            id = "albumId",
            albumUrl = "albumUrl",
            name = "albumName",
            releaseDate = "2024",
            albumType = "Album",
            saveTime = System.currentTimeMillis(),
            artists = listOf(artists),
            copyrights = listOf(copyrights),
            images = "imageUrl"
        )

        val trackItem = TrackEntity(
            id = "trackId",
            durationMs = 12,
            name = "trackName",
            explicit = false,
            albumId = "albumId",
            artists = "artistName"
        )

        val savedAlbums = listOf(albumItem)
        val listenLaterAlbums = listOf(albumItem)
        val tracksAlbum = listOf(trackItem)

        coEvery { repository.getSavedAlbums() } returns savedAlbums
        coEvery { repository.getListenLaterAlbums() } returns listenLaterAlbums
        coEvery { trackRepository.getTracksById("albumId") } returns listOf(
            listOf(trackItem)
        ).flatten()

        val jsonResult = generateJsonData(repository, trackRepository)

        val expectedJson = GsonBuilder().setPrettyPrinting().create().toJson(
            AlbumsData(savedAlbums, listenLaterAlbums, tracksAlbum)
        )

        assertThat(jsonResult).isEqualTo(expectedJson)
    }
}