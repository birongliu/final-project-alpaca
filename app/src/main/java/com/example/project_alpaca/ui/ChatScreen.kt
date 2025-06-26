package com.example.project_alpaca.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMentor: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()
    val dateFormatter = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Mentor Avatar
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Icon(
                                Icons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Column {
                            Text(
                                "Your Mentor",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "Online",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    FloatingActionButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                messages = messages + ChatMessage(content = messageText)
                                messageText = ""
                                // Simulate mentor response after 1 second
                                kotlinx.coroutines.MainScope().launch {
                                    kotlinx.coroutines.delay(1000)
                                    messages = messages + ChatMessage(
                                        content = "I'm here to help! What would you like to discuss?",
                                        isFromMentor = true
                                    )
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Rounded.Send, contentDescription = "Send message")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                ChatMessageBubble(
                    message = message,
                    dateFormatter = dateFormatter
                )
            }
        }

        // Auto-scroll to bottom when new messages arrive
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    dateFormatter: SimpleDateFormat,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromMentor) Alignment.Start else Alignment.End
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (message.isFromMentor) 4.dp else 20.dp,
                bottomEnd = if (message.isFromMentor) 20.dp else 4.dp
            ),
            color = if (message.isFromMentor)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.primary,
            modifier = Modifier.widthIn(max = 340.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
            ) {
                Text(
                    text = message.content,
                    color = if (message.isFromMentor)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Text(
            text = dateFormatter.format(Date(message.timestamp)),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(4.dp)
        )
    }
} 