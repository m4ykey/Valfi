package com.m4ykey.authentication.data.repository

import com.m4ykey.authentication.data.dao.KeyDao
import com.m4ykey.authentication.data.model.KeyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KeyRepository @Inject constructor(
    private val keyDao: KeyDao
) {

    suspend fun insertKeys(keyEntity: KeyEntity) {
        return keyDao.insertKeys(keyEntity)
    }

    fun getKeys() : Flow<KeyEntity?> {
        return keyDao.getKeys()
    }

    suspend fun deleteKeys() {
        return keyDao.deleteKeys()
    }

}