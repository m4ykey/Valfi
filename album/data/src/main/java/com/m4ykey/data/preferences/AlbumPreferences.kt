package com.m4ykey.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.m4ykey.core.views.sorting.SortType
import com.m4ykey.core.views.sorting.ViewType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Singleton

@Singleton
class AlbumPreferences {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "album_preferences")

    companion object {
        private val KEY_SELECTED_ALBUM_TYPE = stringPreferencesKey("selected_album_type")
        private val KEY_SELECTED_VIEW_TYPE = stringPreferencesKey("selected_view_type")
        private val KEY_SELECTED_SORT_TYPE = stringPreferencesKey("selected_sort_type")
    }

    suspend fun saveSelectedSortType(context: Context, sortType: SortType) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_SORT_TYPE] = sortType.name
        }
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

    suspend fun deleteSelectedSortType(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_SORT_TYPE)
        }
    }

    suspend fun deleteSelectedViewType(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_VIEW_TYPE)
        }
    }

    suspend fun deleteSelectedAlbumType(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_ALBUM_TYPE)
        }
    }

    suspend fun getSelectedViewType(context: Context) : ViewType? {
        val viewType = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> preferences[KEY_SELECTED_VIEW_TYPE] }.first()

        return viewType?.let { ViewType.valueOf(it) }
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
            .map { preferences -> preferences[KEY_SELECTED_ALBUM_TYPE] }.first()
    }

    suspend fun getSelectedSortType(context: Context) : SortType? {
        val sortType = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> preferences[KEY_SELECTED_SORT_TYPE] }.first()

        return sortType?.let { SortType.valueOf(it) }
    }
}