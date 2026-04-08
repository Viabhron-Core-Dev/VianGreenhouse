package com.foss.aihub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VGHSettingsScreen(
    onBack: () -> Unit
) {
    var gitRepoUrl by remember { mutableStateOf("https://github.com/user/ai-knowledge-base.git") }
    var gitBranch by remember { mutableStateOf("main") }
    var autoSyncEnabled by remember { mutableStateOf(true) }
    var experimentalFeatures by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VGH Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Git Configuration Section
            SettingsSection(title = "GIT INTEGRATION") {
                OutlinedTextField(
                    value = gitRepoUrl,
                    onValueChange = { gitRepoUrl = it },
                    label = { Text("Repository URL") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = gitBranch,
                    onValueChange = { gitBranch = it },
                    label = { Text("Branch") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.AccountTree, contentDescription = null) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto-Sync", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Automatically push notes and prompts to Git",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = autoSyncEnabled,
                        onCheckedChange = { autoSyncEnabled = it }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Trigger manual sync */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.Sync, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sync Now")
                }
            }

            // Account Section
            SettingsSection(title = "ACCOUNT") {
                ListItem(
                    headlineContent = { Text("VGH Pro Status") },
                    supportingContent = { Text("Active until Dec 2026") },
                    leadingContent = { Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    trailingContent = { TextButton(onClick = { /* Manage */ }) { Text("Manage") } }
                )
            }

            // Advanced Section
            SettingsSection(title = "ADVANCED") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Experimental Features", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Enable beta VGH tools and harvesting algorithms",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = experimentalFeatures,
                        onCheckedChange = { experimentalFeatures = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                "VGH Fork v1.0.0-industrial\nBased on AI Hub Open Source",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}
