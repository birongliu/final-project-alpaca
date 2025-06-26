package com.example.project_alpaca.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.project_alpaca.data.*
import java.util.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksScreen(navController: NavController) {
    val taskManager = LocalTaskManager.current
    val tasks by taskManager.tasks.collectAsState()
    var showAddTaskDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            "Daily Tasks",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "${tasks.count { !it.isCompleted }} tasks remaining",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                modifier = Modifier.semantics {
                    contentDescription = "Add new task button"
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add task")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (tasks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No tasks yet",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Add your first task to get started",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(
                        items = tasks.sortedWith(
                            compareBy<Task> { it.isCompleted }
                                .thenBy { it.priority.ordinal }
                                .thenBy { it.startDate }
                        ),
                        key = { it.id }
                    ) { task ->
                        TaskItem(
                            task = task,
                            onTaskClick = { 
                                if (task.isCompleted) {
                                    taskManager.resetTask(task.id)
                                } else if (task.inProgress) {
                                    taskManager.completeTask(task.id)
                                } else {
                                    taskManager.startTask(task.id)
                                }
                            },
                            onDeleteClick = { taskManager.deleteTask(task.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onTaskAdded = { title, description, category, priority, startDate, endDate ->
                taskManager.addTask(
                    Task(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        category = category,
                        priority = priority,
                        startDate = startDate,
                        endDate = endDate
                    )
                )
                showAddTaskDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dateFormatter = remember { java.text.SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { expanded = !expanded }
            .semantics {
                contentDescription = "Task: ${task.title}, Status: ${
                    when {
                        task.isCompleted -> "Completed"
                        task.inProgress -> "In Progress"
                        else -> "Not Started"
                    }
                }"
            },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                task.isCompleted -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (task.isCompleted) 0.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title and Category Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = if (task.isCompleted) 
                            MaterialTheme.colorScheme.onSurfaceVariant 
                        else 
                            MaterialTheme.colorScheme.onSurface
                    )
                    
                    // Category chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(task.category.color).copy(alpha = 0.12f),
                        modifier = Modifier.height(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                getIconForCategory(task.category),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(task.category.color)
                            )
                            Text(
                                text = task.category.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(task.category.color)
                            )
                        }
                    }
                }

                // Delete button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f))
                        .clickable(onClick = onDeleteClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Delete task",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Dates
            Text(
                text = "${dateFormatter.format(task.startDate)} - ${dateFormatter.format(task.endDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Expanded content
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Description
                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Priority chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when (task.priority) {
                            TaskPriority.HIGH -> Color(0xFFFFEBEE).copy(alpha = 0.7f)
                            TaskPriority.MEDIUM -> Color(0xFFFFF3E0).copy(alpha = 0.7f)
                            TaskPriority.LOW -> Color(0xFFF1F8E9).copy(alpha = 0.7f)
                        },
                        modifier = Modifier.height(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                getIconForPriority(task.priority),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = when (task.priority) {
                                    TaskPriority.HIGH -> Color(0xFFE53935)
                                    TaskPriority.MEDIUM -> Color(0xFFFF8F00)
                                    TaskPriority.LOW -> Color(0xFF7CB342)
                                }
                            )
                            Text(
                                text = task.priority.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = when (task.priority) {
                                    TaskPriority.HIGH -> Color(0xFFE53935)
                                    TaskPriority.MEDIUM -> Color(0xFFFF8F00)
                                    TaskPriority.LOW -> Color(0xFF7CB342)
                                }
                            )
                        }
                    }

                    // Status button
                    Button(
                        onClick = onTaskClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                task.isCompleted -> MaterialTheme.colorScheme.surfaceVariant
                                task.inProgress -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                    ) {
                        Text(
                            when {
                                task.isCompleted -> "Mark as Incomplete"
                                task.inProgress -> "Complete Task"
                                else -> "Start Task"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getIconForCategory(category: TaskCategory): ImageVector = when(category) {
    TaskCategory.PERSONAL -> Icons.Rounded.Person
    TaskCategory.WORK -> Icons.Rounded.Work
    TaskCategory.HEALTH -> Icons.Rounded.Favorite
    TaskCategory.LEARNING -> Icons.Rounded.School
}

@Composable
private fun getIconForPriority(priority: TaskPriority): ImageVector = when(priority) {
    TaskPriority.HIGH -> Icons.Rounded.PriorityHigh
    TaskPriority.MEDIUM -> Icons.Rounded.DragHandle
    TaskPriority.LOW -> Icons.Rounded.ArrowDownward
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onTaskAdded: (String, String, TaskCategory, TaskPriority, Long, Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.PERSONAL) }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var startDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }
    var endDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis + 24 * 60 * 60 * 1000) }
    var isExpanded by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "New Task",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Title field
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Title",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Enter task title") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        )
                    }

                    // Description field
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Description",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Enter task description") },
                            minLines = 3,
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Category dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        ExposedDropdownMenuBox(
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCategory.name,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary
                                ),
                                leadingIcon = {
                                    Icon(
                                        getIconForCategory(selectedCategory),
                                        contentDescription = null,
                                        tint = Color(selectedCategory.color),
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                modifier = Modifier.exposedDropdownSize()
                            ) {
                                TaskCategory.entries.forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    getIconForCategory(category),
                                                    contentDescription = null,
                                                    tint = Color(category.color),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Text(category.name)
                                            }
                                        },
                                        onClick = {
                                            selectedCategory = category
                                            isExpanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onTaskAdded(
                                    title,
                                    description,
                                    selectedCategory,
                                    selectedPriority,
                                    startDate,
                                    endDate
                                )
                            }
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
} 