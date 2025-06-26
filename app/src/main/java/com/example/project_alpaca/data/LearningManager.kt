package com.example.project_alpaca.data

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class LearningManager(private val context: Context) {
    private val _modules = MutableStateFlow<List<SerializableLearningModule>>(emptyList())
    val modules: StateFlow<List<SerializableLearningModule>> = _modules.asStateFlow()

    private val _selectedCategory = MutableStateFlow<LearningCategory?>(null)
    val selectedCategory: StateFlow<LearningCategory?> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val categories = listOf(
        LearningCategory("jobs", "Jobs & Career", 0),
        LearningCategory("life", "Life Skills", 1),
        LearningCategory("social", "Social Skills", 2)
    )

    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        _isLoading.value = true
        _error.value = null
        
        try {
            val sampleModules = listOf(
                SerializableLearningModule(
                    id = UUID.randomUUID().toString(),
                    title = "For Jacob: Working at UVM Hospital",
                    description = "Practice tasks like ordering IV fluids, bandages, and other hospital supplies.",
                    duration = "30 min",
                    categoryIndex = 0,
                    progress = 0.55f,
                    isFeatured = false,
                    imageUrl = "",
                    price = 0
                ),
                SerializableLearningModule(
                    id = UUID.randomUUID().toString(),
                    title = "Finding Out What I Like",
                    description = "Learn about different jobs like working with animals, fixing things, helping in hospitals, and more.",
                    duration = "10 min",
                    categoryIndex = 0,
                    progress = 1.0f,
                    isFeatured = false,
                    imageUrl = "",
                    price = 0
                ),
                SerializableLearningModule(
                    id = UUID.randomUUID().toString(),
                    title = "Looking for a Job",
                    description = "Find out how to search for jobs using websites, newspapers, job fairs, and by asking people you know.",
                    duration = "10 min",
                    categoryIndex = 0,
                    progress = 0.15f,
                    isFeatured = false,
                    imageUrl = "",
                    price = 0
                ),
                SerializableLearningModule(
                    id = UUID.randomUUID().toString(),
                    title = "Getting Ready to Apply",
                    description = "Practice writing a resume, filling out job forms, and learning what to say when people ask for references.",
                    duration = "15 min",
                    categoryIndex = 0,
                    progress = 0.2f,
                    isFeatured = false,
                    imageUrl = "",
                    price = 0
                ),
                SerializableLearningModule(
                    id = UUID.randomUUID().toString(),
                    title = "Doing a Job Interview",
                    description = "Get tips for how to act before, during, and after an interview. Practice answering questions using the S.T.A.R. method.",
                    duration = "15 min",
                    categoryIndex = 0,
                    progress = 0.4f,
                    isFeatured = false,
                    imageUrl = "",
                    price = 0
                ),
                SerializableLearningModule(
                    id = UUID.randomUUID().toString(),
                    title = "Doing Well at Work",
                    description = "Learn how to talk to coworkers, be part of a team, help customers, and stay calm when things get hard.",
                    duration = "20 min",
                    categoryIndex = 0,
                    progress = 0.45f,
                    isFeatured = false,
                    imageUrl = "",
                    price = 0
                ),
                SerializableLearningModule(
                    id = UUID.randomUUID().toString(),
                    title = "Other Job Stuff You Should Know",
                    description = "Learn about your rights at work, how to quit a job the right way, how to get to work, and what to do if someone treats you unfairly.",
                    duration = "20 min",
                    categoryIndex = 0,
                    progress = 0.65f,
                    isFeatured = false,
                    imageUrl = "",
                    price = 0
                )
            )
            _modules.value = sampleModules
        } catch (e: Exception) {
            _error.value = "Failed to load courses: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun setSelectedCategory(category: LearningCategory?) {
        _selectedCategory.value = category
    }

    fun updateModuleProgress(moduleId: String, progress: Float) {
        _modules.update { modules ->
            modules.map { module ->
                if (module.id == moduleId) {
                    module.copy(progress = progress)
                } else {
                    module
                }
            }
        }
    }
}

val LocalLearningManager = compositionLocalOf<LearningManager> { 
    error("No LearningManager provided") 
} 