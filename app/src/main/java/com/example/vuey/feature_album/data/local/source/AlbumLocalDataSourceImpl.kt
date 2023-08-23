package com.example.vuey.feature_album.data.local.source

import com.example.vuey.feature_album.data.local.dao.AlbumDao
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.local.entity.ListenLaterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumLocalDataSourceImpl @Inject constructor(
    private val albumDao: AlbumDao
) : AlbumLocalDataSource {

    override suspend fun insertAlbum(albumEntity: AlbumEntity) {
        return albumDao.insertAlbum(albumEntity)
    }

    override suspend fun deleteAlbum(albumEntity: AlbumEntity) {
        return albumDao.deleteAlbum(albumEntity)
    }

    override fun getAllAlbums(): Flow<List<AlbumEntity>> {
        return albumDao.getAllAlbums()
    }

    override fun getAlbumById(albumId: String): Flow<AlbumEntity> {
        return albumDao.getAlbumById(albumId)
    }

    override fun getAlbumCount(): Flow<Int> {
        return albumDao.getAlbumCount()
    }

    override fun getTotalTracks(): Flow<Int> {
        return albumDao.getTotalTracks()
    }

    override fun getTotalLength(): Flow<Int> {
        return albumDao.getTotalLength()
    }

    override suspend fun insertAlbumToListenLater(listenLaterEntity: ListenLaterEntity) {
        return albumDao.insertAlbumToListenLater(listenLaterEntity)
    }

    override suspend fun deleteAlbumToListenLater(listenLaterEntity: ListenLaterEntity) {
        return albumDao.deleteAlbumToListenLater(listenLaterEntity)
    }

    override fun getAllListenLaterAlbums(): Flow<List<ListenLaterEntity>> {
        return albumDao.getAllListenLaterAlbums()
    }

    override fun getListenLaterAlbumById(albumId: String): Flow<ListenLaterEntity> {
        return albumDao.getListenLaterAlbumById(albumId)
    }
}