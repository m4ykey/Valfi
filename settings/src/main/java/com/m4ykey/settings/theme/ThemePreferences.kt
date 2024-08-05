package com.m4ykey.settings.theme

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
class ThemePreferences {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

    companion object {
        private val KEY_SELECTED_THEME = stringPreferencesKey("selected_theme")
    }

    suspend fun saveThemeOptions(context: Context, selectedTheme : ThemeOptions) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_THEME] = selectedTheme.name
        }
    }

    suspend fun deleteThemeOptions(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_THEME)
        }
    }

    suspend fun getSelectedThemeOptions(context: Context) : ThemeOptions? {
        val themeOption = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> preferences[KEY_SELECTED_THEME] }.first()

        return themeOption?.let { ThemeOptions.valueOf(it) }
    }

}