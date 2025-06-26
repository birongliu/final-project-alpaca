package com.example.project_alpaca.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_alpaca.data.LocalLearningManager
import com.example.project_alpaca.data.LocalTaskManager
import com.example.project_alpaca.data.SerializableLearningModule
import com.example.project_alpaca.data.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val learningManager = LocalLearningManager.current
    val taskManager = LocalTaskManager.current
    
    val modules by learningManager.modules.collectAsState()
    val tasks by taskManager.tasks.collectAsState()
    
    // Calculate statistics
    val completedModules = modules.count { it.progress >= 1.0f }
    val totalModules = modules.size
    val learningProgress = if (totalModules > 0) {
        modules.map { it.progress }.average().toFloat()
    } else 0f
    
    val completedTasks = tasks.count { it.isCompleted }
    val totalTasks = tasks.size
    val taskProgress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f
    
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val today = dateFormat.format(Date())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            "My Progress",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            today,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FF)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OverallProgressCard(
                    learningProgress = learningProgress,
                    taskProgress = taskProgress,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            item {
                StatisticsSection(
                    completedModules = completedModules,
                    totalModules = totalModules,
                    completedTasks = completedTasks,
                    totalTasks = totalTasks,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            item {
                RecentActivitySection(
                    modules = modules.filter { it.progress > 0f }.sortedByDescending { it.progress },
                    tasks = tasks.filter { it.isCompleted }.takeLast(5),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun OverallProgressCard(
    learningProgress: Float,
    taskProgress: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Overall Progress",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProgressItem(
                    label = "Learning Progress",
                    progress = learningProgress,
                    icon = Icons.Default.School
                )
                ProgressItem(
                    label = "Task Completion",
                    progress = taskProgress,
                    icon = Icons.Default.CheckCircle
                )
            }
        }
    }
}

@Composable
private fun ProgressItem(
    label: String,
    progress: Float,
    icon: ImageVector
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Text(
                "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun StatisticsSection(
    completedModules: Int,
    totalModules: Int,
    completedTasks: Int,
    totalTasks: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Statistics",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                title = "Modules Completed",
                value = "$completedModules/$totalModules",
                icon = Icons.Default.LibraryBooks,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Tasks Completed",
                value = "$completedTasks/$totalTasks",
                icon = Icons.Default.Task,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun RecentActivitySection(
    modules: List<SerializableLearningModule>,
    tasks: List<Task>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Recent Activity",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        if (modules.isEmpty() && tasks.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "No recent activity",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Complete some tasks or modules to see them here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                modules.take(3).forEach { module ->
                    ActivityItem(
                        title = module.title,
                        subtitle = "Module completed",
                        icon = Icons.Default.School,
                        progress = module.progress
                    )
                }
                
                tasks.forEach { task ->
                    ActivityItem(
                        title = task.title,
                        subtitle = "Task completed",
                        icon = Icons.Default.Task,
                        progress = 1f
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    progress: Float
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
} 