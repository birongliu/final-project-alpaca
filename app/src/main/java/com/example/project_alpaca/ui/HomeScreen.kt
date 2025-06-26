package com.example.project_alpaca.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_alpaca.R
import com.example.project_alpaca.data.LocalTaskManager
import com.example.project_alpaca.ui.theme.ProjectalpacaTheme
import java.util.Calendar
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(
    userName: String = "Jacob",
    onDailyChecklistClick: () -> Unit = {},
    onLearningDashboardClick: () -> Unit = {},
    onChatWithMentorClick: () -> Unit = {},
    onMyProgressClick: () -> Unit = {},
    onAccessibilitySettingsClick: () -> Unit = {},
    navController: NavController
) {
    val taskManager = LocalTaskManager.current
    val tasks by taskManager.tasks.collectAsState()
    val pendingTasksCount = tasks.count { !it.isCompleted }
    val backgroundColor = Color(0xFFF8F9FF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Welcome Section
            Text(
                buildAnnotatedString {
                    append("Welcome back,\n")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(userName)
                    }
                },
                fontSize = 28.sp,
                color = Color(0xFF1A237E),
                modifier = Modifier
                    .padding(top = 32.dp)
                    .semantics {
                        contentDescription = "Welcome back, $userName"
                    }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Actions Section
            Text(
                "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
                modifier = Modifier.semantics {
                    contentDescription = "Quick Actions section"
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Feature Cards Grid with new design
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.ic_checklist,
                    iconTint = Color(0xFF4CAF50),
                    title = "Daily Tasks",
                    subtitle = if (pendingTasksCount == 1) "1 task pending" else "$pendingTasksCount tasks pending",
                    backgroundColor = Color(0xFFE8F5E9),
                    onClick = onDailyChecklistClick,
                    contentDescription = "Daily Tasks card, $pendingTasksCount tasks pending"
                )
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.ic_checklist,
                    iconTint = Color(0xFF2196F3),
                    title = "Learn",
                    subtitle = "30% Complete",
                    backgroundColor = Color(0xFFE3F2FD),
                    onClick = onLearningDashboardClick,
                    contentDescription = "Learning card, 30% complete"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.ic_checklist,
                    iconTint = Color(0xFFF44336),
                    title = "Chat",
                    subtitle = "Talk to mentor",
                    backgroundColor = Color(0xFFFFEBEE),
                    onClick = { navController.navigate("chat") },
                    contentDescription = "Chat card, talk to your mentor"
                )
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.ic_checklist,
                    iconTint = Color(0xFF9C27B0),
                    title = "Progress",
                    subtitle = "View stats",
                    backgroundColor = Color(0xFFF3E5F5),
                    onClick = onMyProgressClick,
                    contentDescription = "Progress card, view your statistics"
                )
            }
        }

        // Accessibility Settings at the bottom
        Card(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clickable(onClick = onAccessibilitySettingsClick)
                .semantics {
                    contentDescription = "Accessibility Settings, customize your experience"
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Accessibility",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A237E)
                    )
                    Text(
                        text = "Customize your experience",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color(0xFF1A237E),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun FeatureCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    iconTint: Color,
    title: String,
    subtitle: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    contentDescription: String
) {
    Card(
        modifier = modifier
            .height(160.dp)
            .clickable(onClick = onClick)
            .semantics {
                this.contentDescription = contentDescription
            },
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProjectalpacaTheme {
        HomeScreen(navController = rememberNavController())
    }
} 