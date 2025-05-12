package com.m4ykey.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.m4ykey.core.di.Prefs
import com.m4ykey.core.safeDataStoreOperations
import com.m4ykey.core.views.sorting.SortType
import com.m4ykey.core.views.sorting.ViewType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Prefs.AlbumPrefs
class AlbumPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "album_preferences")

    companion object {
        private val KEY_SELECTED_ALBUM_TYPE = stringPreferencesKey("selected_album_type")
        private val KEY_SELECTED_VIEW_TYPE = stringPreferencesKey("selected_view_type")
        private val KEY_SELECTED_SORT_TYPE = stringPreferencesKey("selected_sort_type")
    }

    suspend fun saveSelectedSortType(sortType: SortType) {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences[KEY_SELECTED_SORT_TYPE] = sortType.name
            }
        }
    }

    suspend fun saveSelectedViewType(viewType: ViewType) {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences[KEY_SELECTED_VIEW_TYPE] = viewType.name
            }
        }
    }

    suspend fun saveSelectedAlbumType(albumType : String) {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences[KEY_SELECTED_ALBUM_TYPE] = albumType
            }
        }
    }

    suspend fun deleteSelectedSortType() {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences.remove(KEY_SELECTED_SORT_TYPE)
            }
        }
    }

    suspend fun deleteSelectedViewType() {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences.remove(KEY_SELECTED_VIEW_TYPE)
            }
        }
    }

    suspend fun deleteSelectedAlbumType() {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences.remove(KEY_SELECTED_ALBUM_TYPE)
            }
        }
    }

    fun getSelectedViewType() : Flow<ViewType?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[KEY_SELECTED_VIEW_TYPE]?.let { ViewType.valueOf(it) }
            }
    }

    fun getSelectedAlbumType() : Flow<String?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> preferences[KEY_SELECTED_ALBUM_TYPE] }
    }

    fun getSelectedSortType() : Flow<SortType?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> preferences[KEY_SELECTED_SORT_TYPE]?.let { SortType.valueOf(it) } }
    }
}