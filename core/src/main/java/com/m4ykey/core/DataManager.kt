package com.m4ykey.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Singleton

@Singleton
class DataManager(context : Context) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "album_preferences")

    companion object {
        private val KEY_SELECTED_ALBUM_TYPE = stringPreferencesKey("selected_album_type")
    }

    suspend fun saveSelectedAlbumType(context: Context, albumType : String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_ALBUM_TYPE] = albumType
        }
    }

    val selectedAlbumType : Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_SELECTED_ALBUM_TYPE]
        }

}