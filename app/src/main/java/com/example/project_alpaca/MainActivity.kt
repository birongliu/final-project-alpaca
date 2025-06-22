package com.example.project_alpaca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_alpaca.data.CredentialsManager
import com.example.project_alpaca.ui.HomeScreen
import com.example.project_alpaca.ui.LoginScreen
import com.example.project_alpaca.ui.common.Logo
import com.example.project_alpaca.ui.theme.ProjectalpacaTheme
import com.example.project_alpaca.ui.theme.RoyalBlue
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hardcode user credentials if they don't exist
        val credentialsManager = CredentialsManager(applicationContext)
        if (!credentialsManager.areCredentialsStored()) {
            credentialsManager.saveCredentials("admin", "password")
        }

        enableEdgeToEdge()
        setContent {
            ProjectalpacaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "loading") {
                    composable("loading") { LoadingScreen(navController = navController) }
                    composable("login") { LoginScreen(navController = navController) }
                    composable("home") { HomeScreen() }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("login") {
            popUpTo("loading") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RoyalBlue)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Logo()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "NextStep",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    ProjectalpacaTheme {
        LoadingScreen(navController = rememberNavController())
    }
}