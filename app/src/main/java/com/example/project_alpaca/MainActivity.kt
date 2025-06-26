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
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.project_alpaca.data.AccessibilityManager
import com.example.project_alpaca.data.CredentialsManager
import com.example.project_alpaca.data.LocalAccessibilityManager
import com.example.project_alpaca.data.TaskManager
import com.example.project_alpaca.data.LocalTaskManager
import com.example.project_alpaca.ui.AccessibilitySettingsScreen
import com.example.project_alpaca.ui.HomeScreen
import com.example.project_alpaca.ui.LoginScreen
import com.example.project_alpaca.ui.common.Logo
import com.example.project_alpaca.ui.theme.AccessibilityTheme
import com.example.project_alpaca.ui.theme.ProjectalpacaTheme
import com.example.project_alpaca.ui.theme.RoyalBlue
import kotlinx.coroutines.delay
import com.example.project_alpaca.ui.DailyTasksScreen
import com.example.project_alpaca.ui.LearningScreen
import com.example.project_alpaca.data.LocalLearningManager
import com.example.project_alpaca.data.LearningManager
import com.example.project_alpaca.ui.ChatScreen
import com.example.project_alpaca.ui.ProgressScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize managers
        val credentialsManager = CredentialsManager(applicationContext)
        val accessibilityManager = AccessibilityManager(applicationContext)
        val taskManager = TaskManager(applicationContext)
        val learningManager = LearningManager(applicationContext)
        
        if (!credentialsManager.areCredentialsStored()) {
            credentialsManager.saveCredentials("jacob", "password")
        }

        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalAccessibilityManager provides accessibilityManager,
                LocalTaskManager provides taskManager,
                LocalLearningManager provides learningManager
            ) {
                ProjectalpacaTheme {
                    AccessibilityTheme {
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = "loading") {
                            composable("loading") { 
                                LoadingScreen(navController = navController)
                            }
                            composable("login") { 
                                LoginScreen(navController = navController)
                            }
                            composable(
                                route = "home/{username}",
                                arguments = listOf(navArgument("username") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val username = backStackEntry.arguments?.getString("username") ?: "User"
                                HomeScreen(
                                    userName = username,
                                    onAccessibilitySettingsClick = {
                                        navController.navigate("accessibility_settings")
                                    },
                                    onDailyChecklistClick = {
                                        navController.navigate("daily_tasks")
                                    },
                                    onLearningDashboardClick = {
                                        navController.navigate("learning")
                                    },
                                    onMyProgressClick = {
                                        navController.navigate("progress")
                                    },
                                    navController = navController
                                )
                            }
                            composable("accessibility_settings") {
                                AccessibilitySettingsScreen(navController = navController)
                            }
                            composable("daily_tasks") {
                                DailyTasksScreen(navController = navController)
                            }
                            composable("learning") {
                                LearningScreen(navController = navController)
                            }
                            composable("chat") {
                                ChatScreen(navController = navController)
                            }
                            composable("progress") {
                                ProgressScreen(navController = navController)
                            }
                        }
                    }
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