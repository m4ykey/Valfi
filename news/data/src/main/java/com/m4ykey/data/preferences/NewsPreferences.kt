package com.m4ykey.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.m4ykey.core.safeDataStoreOperations
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "news_preferences")

    companion object {
        private val KEY_SELECTED_LIST_TYPE = stringPreferencesKey("selected_view_type")
    }

    suspend fun saveSelectedListType(listType: ListType) {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences[KEY_SELECTED_LIST_TYPE] = listType.name
            }
        }
    }

    suspend fun deleteSelectedListType() {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences.remove(KEY_SELECTED_LIST_TYPE)
            }
        }
    }

    fun getSelectedListType() : Flow<ListType?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> preferences[KEY_SELECTED_LIST_TYPE]?.let { ListType.valueOf(it) } }
    }

}