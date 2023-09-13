package com.m4ykey.repository.album

import com.m4ykey.local.album.dao.AlbumDao
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumLocalRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao
) : AlbumLocalRepository {

    override suspend fun insertAlbum(albumEntity: AlbumEntity) = albumDao.insertAlbum(albumEntity)

    override suspend fun deleteAlbum(albumEntity: AlbumEntity) = albumDao.deleteAlbum(albumEntity)

    override fun getAllAlbums(): Flow<List<AlbumEntity>> = albumDao.getAllAlbums()

    override fun getAlbumById(albumId: String): Flow<AlbumEntity> = albumDao.getAlbumById(albumId)

    override fun getAlbumCount(): Flow<Int> = albumDao.getAlbumCount()

    override fun getTotalTracks(): Flow<Int> = albumDao.getTotalTracks()

    override fun getTotalLength(): Flow<Int> = albumDao.getTotalLength()

    override suspend fun insertAlbumToListenLater(listenLaterEntity: ListenLaterEntity) =
        albumDao.insertAlbumToListenLater(listenLaterEntity)

    override suspend fun deleteAlbumToListenLater(listenLaterEntity: ListenLaterEntity) =
        albumDao.deleteAlbumToListenLater(listenLaterEntity)

    override fun getAllListenLaterAlbums(): Flow<List<ListenLaterEntity>> =
        albumDao.getAllListenLaterAlbums()

    override fun getListenLaterAlbumById(albumId: String): Flow<ListenLaterEntity> =
        albumDao.getListenLaterAlbumById(albumId)

    override fun getListenLaterAlbumCount(): Flow<Int> = albumDao.getListenLaterAlbumCount()
}