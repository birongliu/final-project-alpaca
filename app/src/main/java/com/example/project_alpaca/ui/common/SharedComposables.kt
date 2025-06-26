package com.example.project_alpaca.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.project_alpaca.data.LocalAccessibilityManager
import com.example.project_alpaca.ui.theme.LogoOrange
import com.example.project_alpaca.ui.theme.LogoYellow

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(150.dp)) {
        Column {
            Row {
                LogoSquare(color = LogoYellow)
                LogoSquare(color = LogoOrange, rotation = -15f, modifier = Modifier.offset(x = (-8).dp, y = 8.dp))
            }
            Row(modifier = Modifier.padding(top = 4.dp)) {
                LogoSquare(color = LogoYellow)
                LogoSquare(color = LogoYellow)
            }
        }
    }
}

@Composable
fun LogoSquare(color: Color, rotation: Float = 0f, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .size(70.dp)
            .rotate(rotation),
        color = color,
        shape = RoundedCornerShape(12.dp)
    ) {}
}

/**
 * Adds screen reader support to a composable.
 * When screen reader is enabled, it will use the provided description.
 * When screen reader is disabled, it will use the default semantics.
 */
@Composable
fun Modifier.screenReaderSupport(
    description: String,
    clearExistingSemantics: Boolean = false
): Modifier {
    val accessibilityManager = LocalAccessibilityManager.current
    val isScreenReaderEnabled by accessibilityManager.isScreenReaderEnabled.collectAsState()

    return if (isScreenReaderEnabled) {
        if (clearExistingSemantics) {
            this.clearAndSetSemantics {
                contentDescription = description
            }
        } else {
            this.semantics {
                contentDescription = description
            }
        }
    } else {
        this
    }
}

/**
 * Merges multiple elements into a single semantic node for screen readers.
 * This is useful for grouping related elements together.
 */
@Composable
fun Modifier.mergeScreenReaderElements(description: String): Modifier {
    val accessibilityManager = LocalAccessibilityManager.current
    val isScreenReaderEnabled by accessibilityManager.isScreenReaderEnabled.collectAsState()

    return if (isScreenReaderEnabled) {
        this.semantics(mergeDescendants = true) {
            contentDescription = description
        }
    } else {
        this
    }
} 