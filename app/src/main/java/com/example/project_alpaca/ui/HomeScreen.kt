package com.example.project_alpaca.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project_alpaca.ui.theme.ProjectalpacaTheme

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Welcome! You are logged in.")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProjectalpacaTheme {
        HomeScreen()
    }
} 