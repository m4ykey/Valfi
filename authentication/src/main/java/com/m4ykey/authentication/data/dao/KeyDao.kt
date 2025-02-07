package com.m4ykey.authentication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.authentication.data.model.KeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(keyEntity: KeyEntity)

    @Query("SELECT * FROM key_table LIMIT 1")
    fun getKeys() : Flow<KeyEntity?>

    @Query("DELETE FROM key_table")
    suspend fun deleteKeys()

}