package com.m4ykey.settings.data.file

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ArtistEntity
import com.m4ykey.data.local.model.CopyrightEntity
import com.m4ykey.data.local.model.TrackEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ReadJsonDataTest {

    private lateinit var context : Context
    private lateinit var testUri : Uri
    private lateinit var testFile : File

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        testFile = File(context.cacheDir, "test.json")
        testUri = Uri.fromFile(testFile)

        val jsonData = GsonBuilder().setPrettyPrinting().create().toJson(
            AlbumsData(
                trackEntity = listOf(trackEntity(id = "track1", name = "Track1", albumId = "1")),
                savedAlbums = listOf(albumEntity(id = "1", name = "Album1")),
                listenLaterAlbums = listOf(albumEntity(id = "2", name = "Album2"))
            )
        )

        testFile.writeText(jsonData)
    }

    @After
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun readJsonDataShouldCorrectlyParseJSON() = runBlocking {
        val result = readJsonData(context, testUri)

        assertEquals("Album1", result?.savedAlbums?.first()?.name)
        assertEquals("Album2", result?.listenLaterAlbums?.first()?.name)
        assertEquals("Track1", result?.trackEntity?.first()?.name)
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