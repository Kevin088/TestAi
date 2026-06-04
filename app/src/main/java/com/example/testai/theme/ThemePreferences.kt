package com.example.testai.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemePreferences {
    private const val PREFS_NAME = "theme_preferences"
    private const val KEY_THEME_MODE = "theme_mode"

    fun getThemeMode(context: Context): ThemeMode {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return ThemeMode.fromStorageValue(prefs.getString(KEY_THEME_MODE, null))
    }

    fun setThemeMode(context: Context, mode: ThemeMode) {
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME_MODE, mode.storageValue)
            .apply()
        applyThemeMode(mode)
    }

    fun applySavedThemeMode(context: Context) {
        applyThemeMode(getThemeMode(context))
    }

    fun applyThemeMode(mode: ThemeMode) {
        AppCompatDelegate.setDefaultNightMode(mode.toNightMode())
    }
}
