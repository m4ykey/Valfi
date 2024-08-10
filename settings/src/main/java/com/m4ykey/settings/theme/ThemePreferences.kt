package com.m4ykey.settings.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Singleton

@Singleton
class ThemePreferences {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

    companion object {
        private val KEY_SELECTED_THEME = intPreferencesKey("selected_theme")
    }

    suspend fun saveThemeOptions(context: Context, selectedTheme : ThemeOptions) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_THEME] = selectedTheme.index
        }
    }

    suspend fun deleteThemeOptions(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_THEME)
        }
    }

    suspend fun getSelectedThemeOptions(context: Context) : ThemeOptions {
        val preferences = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.first()

        val index = preferences[KEY_SELECTED_THEME] ?: 2

        return ThemeOptions.fromIndex(index)
    }

}