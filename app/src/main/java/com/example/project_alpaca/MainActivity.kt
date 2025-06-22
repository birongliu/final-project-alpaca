package com.example.project_alpaca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_alpaca.ui.theme.LogoOrange
import com.example.project_alpaca.ui.theme.LogoYellow
import com.example.project_alpaca.ui.theme.ProjectalpacaTheme
import com.example.project_alpaca.ui.theme.RoyalBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectalpacaTheme {
                LandingScreen()
            }
        }
    }
}

@Composable
fun LandingScreen() {
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
        Text(
            text = "Project Search",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun Logo() {
    Box(modifier = Modifier.size(150.dp)) {
        Column {
            Row {
                LogoSquare(color = LogoYellow)
                LogoSquare(color = LogoOrange, rotation = -15f, modifier = Modifier.offset(x = (-8).dp, y = 8.dp))
            }
            Row(modifier = Modifier.padding(top = 4.dp)) {
                LogoSquare(color = LogoYellow)
                LogoSquare(color = LogoYellow)
            }
        }
    }
}

@Composable
fun LogoSquare(color: Color, rotation: Float = 0f, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .size(70.dp)
            .rotate(rotation),
        color = color,
        shape = RoundedCornerShape(12.dp)
    ) {}
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    ProjectalpacaTheme {
        LandingScreen()
    }
}