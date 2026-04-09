package com.foss.viangreenhouse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HarvestedTag(
    val id: String,
    val name: String,
    val count: Int,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VGHBridgeScreen(
    onBack: () -> Unit
) {
    // Mock data for harvested tags - in a real app, these would be extracted from conversations
    val tags = remember {
        mutableStateListOf(
            HarvestedTag("1", "Architecture", 12, Color(0xFF2196F3)),
            HarvestedTag("2", "Security", 8, Color(0xFFF44336)),
            HarvestedTag("3", "Performance", 15, Color(0xFF4CAF50)),
            HarvestedTag("4", "UI/UX", 22, Color(0xFFFF9800)),
            HarvestedTag("5", "Database", 6, Color(0xFF9C27B0))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VGH Bridge") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Trigger manual harvest */ }) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = "Harvest Tags")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                "TAG HARVESTER",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Insights extracted from your recent AI conversations across all assistants.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tags) { tag ->
                    TagItem(tag)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "The Bridge syncs context between ChatGPT, Claude, and Gemini to prevent redundant prompting.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun TagItem(tag: HarvestedTag) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(tag.color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                tag.name.take(1),
                color = tag.color,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(tag.name, style = MaterialTheme.typography.titleSmall)
            Text("${tag.count} mentions", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        IconButton(onClick = { /* View mentions */ }) {
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}
