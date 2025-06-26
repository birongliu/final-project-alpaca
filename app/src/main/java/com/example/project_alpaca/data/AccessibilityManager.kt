package com.example.project_alpaca.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccessibilityManager(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "accessibility_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _textSizeMultiplier = MutableStateFlow(
        sharedPreferences.getFloat("text_size_multiplier", 1f)
    )
    val textSizeMultiplier: StateFlow<Float> = _textSizeMultiplier.asStateFlow()

    private val _isHighContrastEnabled = MutableStateFlow(
        sharedPreferences.getBoolean("high_contrast_enabled", false)
    )
    val isHighContrastEnabled: StateFlow<Boolean> = _isHighContrastEnabled.asStateFlow()

    private val _isReducedMotionEnabled = MutableStateFlow(
        sharedPreferences.getBoolean("reduced_motion_enabled", false)
    )
    val isReducedMotionEnabled: StateFlow<Boolean> = _isReducedMotionEnabled.asStateFlow()

    private val _isScreenReaderEnabled = MutableStateFlow(
        sharedPreferences.getBoolean("screen_reader_enabled", false)
    )
    val isScreenReaderEnabled: StateFlow<Boolean> = _isScreenReaderEnabled.asStateFlow()

    private val _isDyslexicFontEnabled = MutableStateFlow(
        sharedPreferences.getBoolean("dyslexic_font_enabled", false)
    )
    val isDyslexicFontEnabled: StateFlow<Boolean> = _isDyslexicFontEnabled.asStateFlow()

    private val _isColorBlindModeEnabled = MutableStateFlow(
        sharedPreferences.getBoolean("color_blind_mode_enabled", false)
    )
    val isColorBlindModeEnabled: StateFlow<Boolean> = _isColorBlindModeEnabled.asStateFlow()

    fun updateTextSize(multiplier: Float) {
        sharedPreferences.edit().putFloat("text_size_multiplier", multiplier).apply()
        _textSizeMultiplier.value = multiplier
    }

    fun updateHighContrast(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("high_contrast_enabled", enabled).apply()
        _isHighContrastEnabled.value = enabled
    }

    fun updateReducedMotion(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("reduced_motion_enabled", enabled).apply()
        _isReducedMotionEnabled.value = enabled
    }

    fun updateScreenReader(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("screen_reader_enabled", enabled).apply()
        _isScreenReaderEnabled.value = enabled
    }

    fun updateDyslexicFont(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("dyslexic_font_enabled", enabled).apply()
        _isDyslexicFontEnabled.value = enabled
    }

    fun updateColorBlindMode(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("color_blind_mode_enabled", enabled).apply()
        _isColorBlindModeEnabled.value = enabled
    }
}

// Composition Local for accessing AccessibilityManager throughout the app
val LocalAccessibilityManager = staticCompositionLocalOf<AccessibilityManager> {
    error("No AccessibilityManager provided")
} 