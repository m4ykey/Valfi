package com.m4ykey.settings.data.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ThemePreferencesTest {

    private lateinit var themePreferences: ThemePreferences
    private val mockDataStore : DataStore<Preferences> = mockk(relaxed = true)

    @Before
    fun setUp() {
        themePreferences = ThemePreferences(mockDataStore)
    }

    @Test
    fun `saveThemeOptions should store selected theme`() = runBlocking {
        coEvery { mockDataStore.updateData(any()) } coAnswers { emptyPreferences() }

        themePreferences.saveThemeOptions(ThemeOptions.Dark)

        coVerify { mockDataStore.updateData(any()) }
    }

    @Test
    fun `deleteThemeOptions should remove saved theme`() = runBlocking {
        coEvery { mockDataStore.updateData(any()) } coAnswers { emptyPreferences() }

        themePreferences.deleteThemeOptions()

        coVerify { mockDataStore.updateData(any()) }
    }

    @Test
    fun `getSelectedThemeOptions should return default if no preference set`() = runBlocking {
        val preferences = emptyPreferences()
        every { mockDataStore.data } returns flowOf(preferences)

        val result = themePreferences.getSelectedThemeOptions().first()
        Truth.assertThat(result).isEqualTo(ThemeOptions.Default)
    }

    @Test
    fun `getSelectedThemeOptions should return stored theme`() = runBlocking {
        val preferences =
            preferencesOf(intPreferencesKey("selected_theme") to ThemeOptions.Light.index)
        every { mockDataStore.data } returns flowOf(preferences)
        println(preferences)

        val result = themePreferences.getSelectedThemeOptions().first()
        Truth.assertThat(result).isEqualTo(ThemeOptions.Light)
    }

    @Test
    fun `getSelectedThemeOptions should handle IOException gracefully`() = runBlocking {
        every { mockDataStore.data } returns flow {
            throw IOException()
        }

        val result = themePreferences.getSelectedThemeOptions().first()

        Truth.assertThat(result).isEqualTo(ThemeOptions.Default)
    }
}