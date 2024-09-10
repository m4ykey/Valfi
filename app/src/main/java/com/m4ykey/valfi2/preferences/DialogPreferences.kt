package com.m4ykey.valfi2.preferences

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DialogPreferences {

    private val Context.dataStore by preferencesDataStore(name = "dialog_preferences")

    companion object {
        private val DIALOG_SHOW_KEY = booleanPreferencesKey(name = "dialog_show_key")
    }

    suspend fun isDialogAlreadyShown(context: Context) : Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[DIALOG_SHOW_KEY] ?: false
            }.first()
    }

    suspend fun setIsDialogShow(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[DIALOG_SHOW_KEY] = true
        }
    }
}