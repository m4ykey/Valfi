package com.m4ykey.core.sort

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.m4ykey.core.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesManagerImpl(context: Context) : PreferencesManager {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "view_type")
    private val dataStore = context.dataStore

    override suspend fun setRecyclerViewType(newViewType: Int, iconResId : Int) {
        dataStore.edit { preferences ->
            preferences[VIEW_TYPE_KEY] = newViewType
            preferences[ICON_KEY] = iconResId
        }
    }

    override fun getRecyclerViewViewType(): Flow<Pair<Int, Int>> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val recyclerViewType = preferences[VIEW_TYPE_KEY] ?: DEFAULT_VIEW_TYPE
                val iconResId = preferences[ICON_KEY] ?: DEFAULT_ICON_RES_ID
                Pair(recyclerViewType, iconResId)
            }
    }

    companion object {
        val VIEW_TYPE_KEY = intPreferencesKey("view_type_key")
        val ICON_KEY = intPreferencesKey("icon_key")
        private const val DEFAULT_VIEW_TYPE = 0
        private val DEFAULT_ICON_RES_ID = R.drawable.ic_list
    }

}