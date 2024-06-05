package com.m4ykey.data.test_dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.m4ykey.core.Constants
import com.m4ykey.data.local.dao.AlbumDao
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.local.model.AlbumEntity
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class AlbumDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db : AlbumDatabase
    private lateinit var dao : AlbumDao

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AlbumDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.albumDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testGetAlbumType() = runBlocking {
        val album1 = AlbumEntity(
            id = "123123124",
            images = "",
            name = "",
            releaseDate = "",
            totalTracks = 1,
            artists = emptyList(),
            albumType = Constants.ALBUM,
            albumUrl = ""
        )

        val album2 = AlbumEntity(
            id = "123123124",
            images = "",
            name = "",
            releaseDate = "",
            totalTracks = 1,
            artists = emptyList(),
            albumType = Constants.ALBUM,
            albumUrl = ""
        )

        val album3 = AlbumEntity(
            id = "123123124",
            images = "",
            name = "",
            releaseDate = "",
            totalTracks = 1,
            artists = emptyList(),
            albumType = Constants.EP,
            albumUrl = ""
        )

        dao.insertAlbum(album2)
        dao.insertAlbum(album1)
        dao.insertAlbum(album3)

        val pagingSource : PagingSource<Int, AlbumEntity> = dao.getAlbumType(Constants.ALBUM)

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        val expectedAlbums = listOf(album1)
        if (loadResult is PagingSource.LoadResult.Page) {
            Assert.assertEquals(expectedAlbums, loadResult.data)
        } else {
            Assert.fail("Load result should be a Page")
        }
    }
}