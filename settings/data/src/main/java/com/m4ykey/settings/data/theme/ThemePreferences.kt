package com.m4ykey.settings.data.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.m4ykey.core.di.Prefs
import com.m4ykey.core.safeDataStoreOperations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Prefs.ThemePrefs
class ThemePreferences @Inject constructor(
    private val dataStore : DataStore<Preferences>
) {

    suspend fun saveThemeOptions(selectedTheme: ThemeOptions) {
        safeDataStoreOperations {
            dataStore.edit { preferences ->
                preferences[intPreferencesKey("selected_theme")] = selectedTheme.index
            }
        }
    }

    suspend fun deleteThemeOptions() {
        safeDataStoreOperations {
            dataStore.edit { preferences ->
                preferences.remove(intPreferencesKey("selected_theme"))
            }
        }
    }

    fun getSelectedThemeOptions(): Flow<ThemeOptions> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                val index = preferences[intPreferencesKey("selected_theme")] ?: ThemeOptions.Default.index
                ThemeOptions.fromIndex(index)
            }
    }
}