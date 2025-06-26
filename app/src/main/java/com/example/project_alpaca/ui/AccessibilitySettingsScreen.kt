package com.example.project_alpaca.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_alpaca.data.LocalAccessibilityManager
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilitySettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val accessibilityManager = LocalAccessibilityManager.current
    
    // Collect states
    val textSize by accessibilityManager.textSizeMultiplier.collectAsState()
    val highContrast by accessibilityManager.isHighContrastEnabled.collectAsState()
    val reduceMotion by accessibilityManager.isReducedMotionEnabled.collectAsState()
    val screenReader by accessibilityManager.isScreenReaderEnabled.collectAsState()
    val dyslexicFont by accessibilityManager.isDyslexicFontEnabled.collectAsState()
    val colorBlindMode by accessibilityManager.isColorBlindModeEnabled.collectAsState()
    
    val scrollState = rememberScrollState()
    val showScrollToTop by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Accessibility Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 24.sp
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(56.dp)
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Go back to previous screen",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FF))
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            // Vision Settings Section
            item {
                CategoryHeader(title = "Vision Settings", icon = Icons.Default.Visibility)
            }

            // Text Size Setting
            item {
                AccessibilitySection(
                    title = "Text Size",
                    icon = Icons.Default.TextFields,
                    description = "Make text larger or smaller throughout the app"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                "Sample Text - Adjust the slider to preview",
                                fontSize = (18 * textSize).sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Slider(
                            value = textSize,
                            onValueChange = { accessibilityManager.updateTextSize(it) },
                            valueRange = 0.8f..2f,
                            steps = 4,
                            modifier = Modifier.height(48.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Smaller", fontSize = 16.sp)
                            Text("Larger", fontSize = 24.sp)
                        }
                    }
                }
            }

            // High Contrast Setting
            item {
                AccessibilitySection(
                    title = "High Contrast",
                    icon = Icons.Default.Contrast,
                    description = "Make text and elements easier to see with increased contrast"
                ) {
                    Switch(
                        checked = highContrast,
                        onCheckedChange = { accessibilityManager.updateHighContrast(it) },
                        modifier = Modifier
                            .height(48.dp)
                            .semantics { contentDescription = if (highContrast) "High contrast mode enabled" else "High contrast mode disabled" }
                    )
                }
            }

            // Color Blind Mode Setting
            item {
                AccessibilitySection(
                    title = "Color Blind Mode",
                    icon = Icons.Default.Palette,
                    description = "Optimize colors for different types of color blindness"
                ) {
                    Switch(
                        checked = colorBlindMode,
                        onCheckedChange = { accessibilityManager.updateColorBlindMode(it) },
                        modifier = Modifier
                            .height(48.dp)
                            .semantics { contentDescription = if (colorBlindMode) "Color blind mode enabled" else "Color blind mode disabled" }
                    )
                }
            }

            // Motion & Interaction Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                CategoryHeader(title = "Motion & Interaction", icon = Icons.Default.TouchApp)
            }

            // Reduce Motion Setting
            item {
                AccessibilitySection(
                    title = "Reduce Motion",
                    icon = Icons.Default.Animation,
                    description = "Remove or reduce animations and motion effects"
                ) {
                    Switch(
                        checked = reduceMotion,
                        onCheckedChange = { accessibilityManager.updateReducedMotion(it) },
                        modifier = Modifier
                            .height(48.dp)
                            .semantics { contentDescription = if (reduceMotion) "Reduced motion enabled" else "Reduced motion disabled" }
                    )
                }
            }

            // Reading & Input Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                CategoryHeader(title = "Reading & Input", icon = Icons.Default.MenuBook)
            }

            // Screen Reader Setting
            item {
                AccessibilitySection(
                    title = "Screen Reader Support",
                    icon = Icons.Default.RecordVoiceOver,
                    description = "Optimize for screen readers and enable additional voice descriptions"
                ) {
                    Switch(
                        checked = screenReader,
                        onCheckedChange = { accessibilityManager.updateScreenReader(it) },
                        modifier = Modifier
                            .height(48.dp)
                            .semantics { contentDescription = if (screenReader) "Screen reader support enabled" else "Screen reader support disabled" }
                    )
                }
            }

            // Dyslexic Font Setting
            item {
                AccessibilitySection(
                    title = "Dyslexic-friendly Font",
                    icon = Icons.Default.FontDownload,
                    description = "Use OpenDyslexic font to make text more readable"
                ) {
                    Switch(
                        checked = dyslexicFont,
                        onCheckedChange = { accessibilityManager.updateDyslexicFont(it) },
                        modifier = Modifier
                            .height(48.dp)
                            .semantics { contentDescription = if (dyslexicFont) "Dyslexic friendly font enabled" else "Dyslexic friendly font disabled" }
                    )
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Scroll to top button with larger touch target
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = showScrollToTop,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                FloatingActionButton(
                    onClick = { 
                        kotlinx.coroutines.runBlocking {
                            scrollState.animateScrollTo(0)
                        }
                    },
                    modifier = Modifier.size(64.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll back to top",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    title: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun AccessibilitySection(
    title: String,
    icon: ImageVector,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = "$title. $description"
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Box(modifier = Modifier.padding(top = 8.dp)) {
                content()
            }
        }
    }
} 