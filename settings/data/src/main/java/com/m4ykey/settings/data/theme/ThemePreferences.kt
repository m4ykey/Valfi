package com.m4ykey.settings.data.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
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
class ThemePreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

    companion object {
        private val KEY_SELECTED_THEME = intPreferencesKey("selected_theme")
    }

    suspend fun saveThemeOptions(selectedTheme: ThemeOptions) {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences[KEY_SELECTED_THEME] = selectedTheme.index
            }
        }
    }

    suspend fun deleteThemeOptions() {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences.remove(KEY_SELECTED_THEME)
            }
        }
    }

    fun getSelectedThemeOptions(): Flow<ThemeOptions> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                val index = preferences[KEY_SELECTED_THEME] ?: 2
                ThemeOptions.fromIndex(index)
            }
    }
}