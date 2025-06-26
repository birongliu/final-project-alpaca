package com.example.project_alpaca.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
data class LearningCategory(
    val id: String,
    val name: String,
    val categoryIndex: Int
)

@Serializable
data class SerializableLearningModule(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val categoryIndex: Int,
    val progress: Float = 0f,
    val isFeatured: Boolean = false,
    val externalUrl: String? = null,
    val imageUrl: String? = null,
    val price: Int? = null,
)

enum class ModuleType {
    COURSE, QUIZ, PRACTICE
} 