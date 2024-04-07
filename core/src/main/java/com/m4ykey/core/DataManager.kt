package com.m4ykey.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.m4ykey.core.views.ViewType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Singleton

@Singleton
class DataManager {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "album_preferences")

    companion object {
        private val KEY_SELECTED_ALBUM_TYPE = stringPreferencesKey("selected_album_type")
        private val KEY_SELECTED_VIEW_TYPE = stringPreferencesKey("selected_view_type")
    }

    suspend fun saveSelectedViewType(context: Context, viewType: ViewType) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_VIEW_TYPE] = viewType.name
        }
    }

    suspend fun saveSelectedAlbumType(context: Context, albumType : String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_ALBUM_TYPE] = albumType
        }
    }

    suspend fun clearSelectedViewType(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_VIEW_TYPE)
        }
    }

    suspend fun clearSelectedAlbumType(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_ALBUM_TYPE)
        }
    }

    suspend fun getSelectedViewType(context: Context) : ViewType? {
        val albumType = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[KEY_SELECTED_VIEW_TYPE]
            }
            .first()

        return albumType?.let { ViewType.valueOf(it) }
    }

    suspend fun getSelectedAlbumType(context: Context) : String? {
        return context.dataStore.data
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
            .first()
    }
}