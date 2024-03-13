package com.m4ykey.data.database

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.model.AlbumEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
        artistUrl : String = "Artist Url",
        isListenLaterSaved : Boolean = true
    ): AlbumEntity {
        return AlbumEntity(id, name, totalTracks, albumUrl, artistUrl, image, artistList, saveTime, albumType, releaseDate, isAlbumSaved, isListenLaterSaved)
    }

    @Test
    fun insertAndRetrieveAlbum() = runBlocking {
        val album = createAlbumEntity()

        dao.insertAlbum(album)
        val retrievedAlbum = dao.getLocalAlbumById(album.id).firstOrNull()

        assertEquals(album, retrievedAlbum)
    }

    @Test
    fun deleteAndRetrieveAlbum() = runBlocking {
        val album = createAlbumEntity()
        dao.insertAlbum(album)

        dao.deleteAlbum(album)
        val retrievedAlbum = dao.getLocalAlbumById(album.id).firstOrNull()

        assertNull(retrievedAlbum)
    }

    @Test
    fun updateAlbumSaved() = runBlocking {
        val album = createAlbumEntity()
        dao.insertAlbum(album)

        dao.updateAlbumSaved(album.id, true)
        val retrievedAlbum = dao.getLocalAlbumById(album.id).first()
        assertTrue(retrievedAlbum.isAlbumSaved)
    }

    @Test
    fun updateListenLater() = runBlocking {
        val album = createAlbumEntity()
        dao.insertAlbum(album)

        dao.updateListenLaterSaved(album.id, true)
        val retrievedAlbum = dao.getLocalAlbumById(album.id).first()
        assertTrue(retrievedAlbum.isListenLaterSaved)
    }

    @Test
    fun getAlbumSortedAlphabetical() = runBlocking {
        val albums = listOf(
            createAlbumEntity(name = "Album A"),
            createAlbumEntity(name = "Album V"),
            createAlbumEntity(name = "Album B")
        )
        albums.forEach { dao.insertAlbum(it) }

        val pagingSource = dao.getAlbumSortedAlphabetical()
        val loadParams = PagingSource.LoadParams.Refresh(0, albums.size, false)
        val loadResult = pagingSource.load(loadParams)

        val loadedData = (loadResult as PagingSource.LoadResult.Page).data

        val sortedAlbums = albums.sortedBy { it.name }
        assertEquals(sortedAlbums, loadedData)
    }

    @Test
    fun getAlbumsRecentlyAdded() = runBlocking {
        val currentTime = System.currentTimeMillis()
        val albums = listOf(
            createAlbumEntity(saveTime = currentTime),
            createAlbumEntity(saveTime = currentTime - 1000),
            createAlbumEntity(saveTime = currentTime - 2000)
        )
        albums.forEach { dao.insertAlbum(it) }

        val pagingSource = dao.getAlbumsRecentlyAdded()
        val loadParams = PagingSource.LoadParams.Refresh(0, albums.size, false)
        val loadResult = pagingSource.load(loadParams)

        val loadedData = (loadResult as PagingSource.LoadResult.Page).data

        val recentlyAdded = albums.sortedByDescending { it.saveTime }
        assertEquals(recentlyAdded, loadedData)
    }

    @Test
    fun getAlbumsByTypeAlbumPaged() = runBlocking {
        val albums = listOf(
            createAlbumEntity(albumType = "Album"),
            createAlbumEntity(albumType = "Album"),
            createAlbumEntity(albumType = "Album")
        )
        albums.forEach { dao.insertAlbum(it) }

        val pagingSource = dao.getAlbumsOfTypeAlbumPaged()
        val loadParams = PagingSource.LoadParams.Refresh(0, albums.size, false)
        val loadResult = pagingSource.load(loadParams)

        val loadedData = (loadResult as PagingSource.LoadResult.Page).data

        val albumsType = loadedData.filter { it.albumType == "Album" }
        assertEquals(albums.count(), albumsType.size)
    }

    @Test
    fun getAlbumsOfTypeEPPaged() = runBlocking {
        val albums = listOf(
            createAlbumEntity(albumType = "EP"),
            createAlbumEntity(albumType = "EP"),
            createAlbumEntity(albumType = "EP")
        )
        albums.forEach { dao.insertAlbum(it) }

        val pagingSource = dao.getAlbumsOfTypeEPPaged()
        val loadParams = PagingSource.LoadParams.Refresh(0, albums.size, false)
        val loadResult = pagingSource.load(loadParams)

        val loadedData = (loadResult as PagingSource.LoadResult.Page).data

        val albumsType = loadedData.filter { it.albumType == "EP" }
        assertEquals(albums.count(), albumsType.size)
    }

    @Test
    fun getAlbumsOfTypeSinglePaged() = runBlocking {
        val albums = listOf(
            createAlbumEntity(albumType = "Single"),
            createAlbumEntity(albumType = "Single"),
            createAlbumEntity(albumType = "Single")
        )
        albums.forEach { dao.insertAlbum(it) }

        val pagingSource = dao.getAlbumsOfTypeSinglePaged()
        val loadParams = PagingSource.LoadParams.Refresh(0, albums.size, false)
        val loadResult = pagingSource.load(loadParams)

        val loadedData = (loadResult as PagingSource.LoadResult.Page).data

        val albumsType = loadedData.filter { it.albumType == "Single" }
        assertEquals(albums.count(), albumsType.size)
    }

    @Test
    fun getAlbumsOfTypeCompilationPaged() = runBlocking {
        val albums = listOf(
            createAlbumEntity(albumType = "Compilation"),
            createAlbumEntity(albumType = "Compilation"),
            createAlbumEntity(albumType = "Compilation")
        )
        albums.forEach { dao.insertAlbum(it) }

        val pagingSource = dao.getAlbumsOfTypeCompilationPaged()
        val loadParams = PagingSource.LoadParams.Refresh(0, albums.size, false)
        val loadResult = pagingSource.load(loadParams)

        val loadedData = (loadResult as PagingSource.LoadResult.Page).data

        val albumsType = loadedData.filter { it.albumType == "Compilation" }
        assertEquals(albums.count(), albumsType.size)
    }

    @Test
    fun getLocalAlbumById() = runBlocking {
        val albumId = "4rfq3w2d3"
        val album = createAlbumEntity(id = albumId)

        dao.insertAlbum(album)

        val flow = dao.getLocalAlbumById(albumId)

        val albumFromFlow = flow.first()

        assertEquals(album, albumFromFlow)
    }

    @Test
    fun getAlbumCount() = runBlocking {
        dao.insertAlbum(createAlbumEntity(name = "Album1", isAlbumSaved = true))
        dao.insertAlbum(createAlbumEntity(name = "Album2", isAlbumSaved = true))
        dao.insertAlbum(createAlbumEntity(name = "Album3", isAlbumSaved = true))

        val albumCount = dao.getAlbumCount()

        assertEquals(3, albumCount)
    }

    @Test
    fun getTotalTracksCount() = runBlocking {
        dao.insertAlbum(createAlbumEntity(name = "Album1", isAlbumSaved = true, totalTracks = 10))
        dao.insertAlbum(createAlbumEntity(name = "Album2", isAlbumSaved = true, totalTracks = 14))
        dao.insertAlbum(createAlbumEntity(name = "Album3", isAlbumSaved = true, totalTracks = 53))

        val expectedTotalTrackCount = 10 + 14 + 53
        val totalTrackCount = dao.getTotalTracksCount()

        assertEquals(expectedTotalTrackCount, totalTrackCount)
    }

    @Test
    fun getListenLaterCount() = runBlocking {
        dao.insertAlbum(createAlbumEntity(name = "Album1", isListenLaterSaved = true))
        dao.insertAlbum(createAlbumEntity(name = "Album2", isListenLaterSaved = true))
        dao.insertAlbum(createAlbumEntity(name = "Album3", isListenLaterSaved = true))

        val albumCount = dao.getListenLaterCount()

        assertEquals(3, albumCount)
    }

    @Test
    fun getRandomAlbum() = runBlocking {
        dao.insertAlbum(createAlbumEntity(name = "Album 1"))
        dao.insertAlbum(createAlbumEntity(name = "Album 2"))
        dao.insertAlbum(createAlbumEntity(name = "Album 3"))

        val randomAlbum = dao.getRandomAlbum()

        assertNotNull(randomAlbum)
    }

}