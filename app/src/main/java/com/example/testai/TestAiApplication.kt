package com.example.testai

import android.app.Application
import com.example.testai.theme.ThemePreferences

class TestAiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemePreferences.applySavedThemeMode(this)
    }
}
