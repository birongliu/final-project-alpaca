package com.example.project_alpaca.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import com.example.project_alpaca.data.AccessibilityManager
import com.example.project_alpaca.data.LocalAccessibilityManager

@Composable
fun AccessibilityTheme(
    content: @Composable () -> Unit
) {
    val accessibilityManager = LocalAccessibilityManager.current
    
    // Collect all accessibility states
    val textSizeMultiplier by accessibilityManager.textSizeMultiplier.collectAsState()
    val isHighContrastEnabled by accessibilityManager.isHighContrastEnabled.collectAsState()
    val isDyslexicFontEnabled by accessibilityManager.isDyslexicFontEnabled.collectAsState()
    val isColorBlindModeEnabled by accessibilityManager.isColorBlindModeEnabled.collectAsState()

    // Adjust text size using density
    val density = LocalDensity.current
    val adjustedDensity = Density(
        density = density.density * textSizeMultiplier,
        fontScale = density.fontScale * textSizeMultiplier
    )

    // Create base MaterialTheme
    val baseColorScheme = MaterialTheme.colorScheme

    // Adjust colors for high contrast and color blind modes
    val colors = when {
        isHighContrastEnabled && isColorBlindModeEnabled -> baseColorScheme.copy(
            // High contrast, color-blind friendly colors
            primary = Color(0xFF2E7D32), // Dark green
            secondary = Color(0xFF0D47A1), // Dark blue
            tertiary = Color(0xFF4A148C), // Dark purple
            background = Color.White,
            surface = Color.White,
            surfaceVariant = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White,
            onBackground = Color.Black,
            onSurface = Color.Black,
            onSurfaceVariant = Color.Black,
            outline = Color.Black,
            outlineVariant = Color.Black,
            scrim = Color.Black,
            inverseSurface = Color.Black,
            inverseOnSurface = Color.White,
            inversePrimary = Color.White,
            surfaceTint = Color.Black,
            surfaceBright = Color.White,
            surfaceContainer = Color.White,
            surfaceContainerHigh = Color.White,
            surfaceContainerHighest = Color.White,
            surfaceContainerLow = Color.White,
            surfaceContainerLowest = Color.White,
            surfaceDim = Color.White,
            error = Color(0xFF7F0000), // Very dark red for high contrast
            onError = Color.White,
            errorContainer = Color(0xFF7F0000),
            onErrorContainer = Color.White
        )
        isHighContrastEnabled -> baseColorScheme.copy(
            // High contrast colors
            primary = Color.Black,
            secondary = Color.Black,
            tertiary = Color.Black,
            background = Color.White,
            surface = Color.White,
            surfaceVariant = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White,
            onBackground = Color.Black,
            onSurface = Color.Black,
            onSurfaceVariant = Color.Black,
            outline = Color.Black,
            outlineVariant = Color.Black,
            scrim = Color.Black,
            inverseSurface = Color.Black,
            inverseOnSurface = Color.White,
            inversePrimary = Color.White,
            surfaceTint = Color.Black,
            surfaceBright = Color.White,
            surfaceContainer = Color.White,
            surfaceContainerHigh = Color.White,
            surfaceContainerHighest = Color.White,
            surfaceContainerLow = Color.White,
            surfaceContainerLowest = Color.White,
            surfaceDim = Color.White,
            error = Color.Black,
            onError = Color.White,
            errorContainer = Color.Black,
            onErrorContainer = Color.White
        )
        isColorBlindModeEnabled -> baseColorScheme.copy(
            // Color-blind friendly colors
            primary = Color(0xFF2E7D32), // Dark green
            secondary = Color(0xFF0D47A1), // Dark blue
            tertiary = Color(0xFF4A148C), // Dark purple
            error = Color(0xFFB71C1C) // Dark red
        )
        else -> baseColorScheme
    }

    // Adjust typography for dyslexic font
    val typography = if (isDyslexicFontEnabled) {
        Typography().copy(
            displayLarge = Typography().displayLarge.copy(fontFamily = FontFamily.Default),
            displayMedium = Typography().displayMedium.copy(fontFamily = FontFamily.Default),
            displaySmall = Typography().displaySmall.copy(fontFamily = FontFamily.Default),
            headlineLarge = Typography().headlineLarge.copy(fontFamily = FontFamily.Default),
            headlineMedium = Typography().headlineMedium.copy(fontFamily = FontFamily.Default),
            headlineSmall = Typography().headlineSmall.copy(fontFamily = FontFamily.Default),
            titleLarge = Typography().titleLarge.copy(fontFamily = FontFamily.Default),
            titleMedium = Typography().titleMedium.copy(fontFamily = FontFamily.Default),
            titleSmall = Typography().titleSmall.copy(fontFamily = FontFamily.Default),
            bodyLarge = Typography().bodyLarge.copy(fontFamily = FontFamily.Default),
            bodyMedium = Typography().bodyMedium.copy(fontFamily = FontFamily.Default),
            bodySmall = Typography().bodySmall.copy(fontFamily = FontFamily.Default),
            labelLarge = Typography().labelLarge.copy(fontFamily = FontFamily.Default),
            labelMedium = Typography().labelMedium.copy(fontFamily = FontFamily.Default),
            labelSmall = Typography().labelSmall.copy(fontFamily = FontFamily.Default)
        )
    } else {
        Typography()
    }

    CompositionLocalProvider(
        LocalDensity provides adjustedDensity
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content
        )
    }
} 