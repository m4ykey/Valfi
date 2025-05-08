package com.m4ykey.settings.data.theme

import org.junit.Assert
import org.junit.Test

class ThemeOptionsTest {

    @Test
    fun `fromIndex should return correct ThemeOptions`() {
        Assert.assertEquals(ThemeOptions.Light, ThemeOptions.fromIndex(0))
        Assert.assertEquals(ThemeOptions.Dark, ThemeOptions.fromIndex(1))
        Assert.assertEquals(ThemeOptions.Default, ThemeOptions.fromIndex(2))
    }

    @Test
    fun `fromIndex should return Default for unknown index`() {
        Assert.assertEquals(ThemeOptions.Default, ThemeOptions.fromIndex(-1))
        Assert.assertEquals(ThemeOptions.Default, ThemeOptions.fromIndex(100))
    }

}