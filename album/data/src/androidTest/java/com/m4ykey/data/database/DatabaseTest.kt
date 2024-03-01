package com.m4ykey.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.model.AlbumEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var db : AlbumDatabase
    private lateinit var dao : AlbumDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AlbumDatabase::class.java
        ).build()
        dao = db.albumDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun createAlbumEntity(
        id: String = UUID.randomUUID().toString(),
        name: String = "Test Album",
        totalTracks : Int = 12,
        image : String = "Image URL",
        artistList: String = "Artist1, Artist2",
        saveTime : Long = System.currentTimeMillis(),
        albumType : String = "Album",
        releaseDate : String = "12 Apr 2020",
        isAlbumSaved : Boolean = true,
        albumUrl : String = "Album Url",
        artistUrl : String = "Artist Url"
    ): AlbumEntity {
        return AlbumEntity(id, name, totalTracks, albumUrl, artistUrl, image, artistList, saveTime, albumType, releaseDate, isAlbumSaved)
    }

    @Test
    fun insertAlbumTest() = runBlocking {
        val album = createAlbumEntity()
        dao.insertAlbum(album)

        val retrievedAlbum = dao.getLocalAlbumById(album.id)
        assertEquals(album, retrievedAlbum)
    }

    @Test
    fun deleteAlbumTest() = runBlocking {
        val album = createAlbumEntity()
        dao.deleteAlbum(album)
        dao.deleteAlbum(album)

        val retrievedAlbum = dao.getLocalAlbumById(album.id)
        assertNull(retrievedAlbum)
    }

    @Test
    fun alphabeticalSortTest() = runBlocking {
        val albums = listOf(
            createAlbumEntity(id = "1", name = "B Album"),
            createAlbumEntity(id = "2", name = "A Album"),
            createAlbumEntity(id = "3", name = "C Album")
        )

        albums.forEach { dao.insertAlbum(it) }

        val sortedAlbums = mutableListOf<AlbumEntity>()

        assertEquals(sortedAlbums.map { it.name }, listOf("A Album", "B Album", "C Album"))
    }

}