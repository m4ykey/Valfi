package com.m4ykey.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Singleton

@Singleton
class NewsPreferences {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "news_preferences")

    companion object {
        private val KEY_SELECTED_LIST_TYPE = stringPreferencesKey("selected_view_type")
    }

    suspend fun saveSelectedListType(context: Context, listType: ListType) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_LIST_TYPE] = listType.name
        }
    }

    suspend fun deleteSelectedListType(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_LIST_TYPE)
        }
    }

    suspend fun getSelectedListType(context: Context) : ListType? {
        val listType = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> preferences[KEY_SELECTED_LIST_TYPE] }.first()

        return listType?.let { ListType.valueOf(it) }
    }

}