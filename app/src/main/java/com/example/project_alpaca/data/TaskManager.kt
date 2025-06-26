package com.example.project_alpaca.data

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.core.content.edit

@Serializable
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val inProgress: Boolean = false,
    val startDate: Long = Calendar.getInstance().timeInMillis,
    val endDate: Long = Calendar.getInstance().timeInMillis + 24 * 60 * 60 * 1000, // Default to next day
    val category: TaskCategory = TaskCategory.PERSONAL,
    val priority: TaskPriority = TaskPriority.MEDIUM
)

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}

enum class TaskCategory(val icon: String, val color: Long) {
    PERSONAL("person", 0xFF9C27B0),
    WORK("work", 0xFF2196F3),
    HEALTH("favorite", 0xFF4CAF50),
    LEARNING("school", 0xFFFFA000)
}

class TaskManager(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val json = Json { ignoreUnknownKeys = true }

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "tasks_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _tasks = MutableStateFlow<List<Task>>(loadTasks())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private fun loadTasks(): List<Task> {
        val tasksJson = sharedPreferences.getString("tasks", null)
        return if (tasksJson != null) {
            try {
                json.decodeFromString(tasksJson)
            } catch (e: Exception) {
                createDefaultTasks()
            }
        } else {
            createDefaultTasks()
        }
    }

    private fun createDefaultTasks(): List<Task> {
        val defaultTasks = listOf(
            Task(
                id = "1",
                title = "Complete Daily Exercise",
                description = "30 minutes of physical activity",
                category = TaskCategory.HEALTH
            ),
            Task(
                id = "2",
                title = "Study Programming",
                description = "Work on coding projects for 1 hour",
                category = TaskCategory.LEARNING
            ),
            Task(
                id = "3",
                title = "Plan Tomorrow",
                description = "Review and plan tasks for tomorrow",
                category = TaskCategory.PERSONAL
            )
        )
        saveTasks(defaultTasks)
        return defaultTasks
    }

    private fun saveTasks(tasks: List<Task>) {
        val tasksJson = json.encodeToString(tasks)
        sharedPreferences.edit { putString("tasks", tasksJson) }
        _tasks.value = tasks
    }

    fun addTask(task: Task) {
        val currentTasks = _tasks.value.toMutableList()
        currentTasks.add(task)
        saveTasks(currentTasks)
    }

    fun updateTask(task: Task) {
        val currentTasks = _tasks.value.toMutableList()
        val index = currentTasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            currentTasks[index] = task
            saveTasks(currentTasks)
        }
    }

    fun completeTask(taskId: String) {
        val currentTasks = _tasks.value.toMutableList()
        val index = currentTasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val task = currentTasks[index]
            currentTasks[index] = task.copy(isCompleted = true, inProgress = false)
            saveTasks(currentTasks)
        }
    }

    fun startTask(taskId: String) {
        val currentTasks = _tasks.value.toMutableList()
        val index = currentTasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val task = currentTasks[index]
            currentTasks[index] = task.copy(inProgress = true, isCompleted = false)
            saveTasks(currentTasks)
        }
    }

    fun resetTask(taskId: String) {
        val currentTasks = _tasks.value.toMutableList()
        val index = currentTasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val task = currentTasks[index]
            currentTasks[index] = task.copy(inProgress = false, isCompleted = false)
            saveTasks(currentTasks)
        }
    }

    fun deleteTask(taskId: String) {
        val currentTasks = _tasks.value.toMutableList()
        currentTasks.removeAll { it.id == taskId }
        saveTasks(currentTasks)
    }

    fun getPendingTasksCount(): Int {
        return _tasks.value.count { !it.isCompleted }
    }
}

val LocalTaskManager = staticCompositionLocalOf<TaskManager> {
    error("No TaskManager provided")
} 