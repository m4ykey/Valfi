package com.m4ykey.settings.data.file

import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.CopyrightEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.TrackEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertAlbumDataTest {

    private lateinit var tRepository: TrackRepository
    private lateinit var aRepository : AlbumRepository

    @Before
    fun setup() {
        tRepository = mockk(relaxed = true)
        aRepository = mockk(relaxed = true)
    }

    @Test
    fun `insertAlbumData should correctly insert albums and tracks`() = runBlocking {
        val album1 = albumEntity(id = "1", name = "Album1")
        val album2 = albumEntity(id = "2", name = "Album2")
        val track1 = trackEntity(id = "track1", name = "Track1", albumId = "1")
        val track2 = trackEntity(id = "track2", name = "Track2", albumId = "2")

        val albumsData = AlbumsData(
            savedAlbums = listOf(album1),
            listenLaterAlbums = listOf(album2),
            trackEntity = listOf(track1, track2)
        )

        coEvery { aRepository.getAlbum("1") } returns null
        coEvery { aRepository.insertAlbum(album1) } just Runs
        coEvery { aRepository.insertSavedAlbum(IsAlbumSaved(albumId = "1", isAlbumSaved = true)) } just Runs
        coEvery { aRepository.insertListenLaterAlbum(IsListenLaterSaved(albumId = "2", isListenLaterSaved = true)) } just Runs
        coEvery { tRepository.insertTracks(listOf(track1, track2)) } just Runs

        insertAlbumData(aRepository, albumsData, tRepository)

        coVerify {
            aRepository.insertAlbum(match {
                it.id == "1" && it.name == "Album1"
            })
        }
        coVerify {
            aRepository.insertAlbum(match {
                it.id == "2" && it.name == "Album2"
            })
        }
        coVerify { aRepository.insertSavedAlbum(IsAlbumSaved(album1.id, true)) }
        coVerify { aRepository.insertListenLaterAlbum(IsListenLaterSaved(album2.id, true)) }
        coVerify { tRepository.insertTracks(listOf(track1, track2)) }
    }

    private fun albumEntity(id : String, name : String) : AlbumEntity {
        val albumEntity = AlbumEntity(
            images = "images",
            albumUrl = "albumUrl",
            albumType = "Album",
            id = id,
            name = name,
            releaseDate = "2024",
            totalTracks = 21,
            copyrights = listOf(CopyrightEntity(text = "copyright")),
            artists = listOf(ArtistEntity(
                url = "artistUrl",
                name = "artistName",
                albumId = "albumId",
                artistId = "artistId"
            ))
        )
        return albumEntity
    }

    private fun trackEntity(id : String, name : String, albumId : String) : TrackEntity {
        val trackEntity = TrackEntity(
            explicit = false,
            durationMs = 12,
            name = name,
            albumId = albumId,
            id = id,
            artists = "artists"
        )

        return trackEntity
    }

}