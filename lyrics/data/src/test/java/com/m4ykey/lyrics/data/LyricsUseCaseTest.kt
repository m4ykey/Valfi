package com.m4ykey.lyrics.data

import com.lyrics.data.domain.model.Album
import com.lyrics.data.domain.model.ExternalUrls
import com.lyrics.data.domain.model.Image
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import com.lyrics.data.domain.repository.LyricsRepository
import com.lyrics.data.domain.usecase.LyricsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LyricsUseCaseTest {

    private lateinit var repository : LyricsRepository
    private lateinit var useCaseTest: LyricsUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCaseTest = LyricsUseCase(repository)
    }

    @Test
    fun `searchLyrics should fetch lyrics from repository`() = runTest {
        val lyricsItem = LyricsItem(
            id = 123,
            artistName = "artistName",
            duration = 0.1,
            name = "lyricsName",
            plainLyrics = "plainLyrics",
            trackName = "trackName"
        )

        coEvery { repository.searchLyrics(trackName = "Test Song", "Test Artist") } returns flowOf(lyricsItem)

        val result = useCaseTest.searchLyrics("Test Song", "Test Artist")

        result.collect { fetchedLyrics ->
            assert(fetchedLyrics == lyricsItem)
        }

        coVerify { repository.searchLyrics("Test Song", "Test Artist") }

    }

    @Test
    fun `getTrackById should fetch track from repository`() = runTest {
        val images = Image(
            height = 123,
            url = "imageUrl",
            width = 123
        )
        val album = Album(
            images = listOf(images)
        )
        val externalUrls = ExternalUrls(spotify = "link")
        val track = Track(id = "123", album = album, externalUrls = externalUrls)

        coEvery { repository.getTrackById("123") } returns flowOf(track)

        val result = useCaseTest.getTrackById("123")

        result.collect { fetchedTrack ->
            assert(fetchedTrack == track)
        }

        coVerify { repository.getTrackById("123") }
    }
}