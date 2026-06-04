package com.example.testai.theme

import androidx.appcompat.app.AppCompatDelegate
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {
    @Test
    fun fromStorageValue_returnsSystemForMissingOrUnknownValue() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromStorageValue(null))
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromStorageValue("unknown"))
    }

    @Test
    fun fromStorageValue_returnsMatchingMode() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromStorageValue("system"))
        assertEquals(ThemeMode.LIGHT, ThemeMode.fromStorageValue("light"))
        assertEquals(ThemeMode.DARK, ThemeMode.fromStorageValue("dark"))
    }

    @Test
    fun toNightMode_mapsToAppCompatModes() {
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, ThemeMode.SYSTEM.toNightMode())
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, ThemeMode.LIGHT.toNightMode())
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, ThemeMode.DARK.toNightMode())
    }
}
