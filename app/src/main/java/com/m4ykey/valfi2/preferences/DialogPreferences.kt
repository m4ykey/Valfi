package com.m4ykey.valfi2.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.m4ykey.core.safeDataStoreOperations
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "dialog_preferences")

    companion object {
        private val DIALOG_SHOW_KEY = booleanPreferencesKey(name = "dialog_show_key")
        private val DIALOG_PERMISSION_KEY = booleanPreferencesKey(name = "dialog_permission_key")
    }

    suspend fun isPermissionGranted(): Boolean {
        return safeDataStoreOperations {
            context.dataStore.data
                .map { preferences ->
                    preferences[DIALOG_PERMISSION_KEY] ?: false
                }.first()
        } ?: false
    }

    suspend fun setIsPermissionGranted()  {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences[DIALOG_PERMISSION_KEY] = true
            }
        }
    }

    suspend fun isDialogAlreadyShown() : Boolean {
        return safeDataStoreOperations {
            context.dataStore.data
                .map { preferences ->
                    preferences[DIALOG_SHOW_KEY] ?: false
                }.first()
        } ?: false
    }

    suspend fun setIsDialogShow() {
        safeDataStoreOperations {
            context.dataStore.edit { preferences ->
                preferences[DIALOG_SHOW_KEY] = true
            }
        }
    }
}