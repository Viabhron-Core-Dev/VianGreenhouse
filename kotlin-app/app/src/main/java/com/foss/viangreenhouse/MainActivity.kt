package com.foss.viangreenhouse

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.foss.viangreenhouse.models.Assistant
import com.foss.viangreenhouse.models.defaultAssistants
import com.foss.viangreenhouse.models.TabType
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import kotlinx.coroutines.launch

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts

import androidx.room.Room
import com.foss.viangreenhouse.db.VGHDatabase
import com.foss.viangreenhouse.db.VGHQuickPrompt
import java.util.UUID

import com.foss.viangreenhouse.ui.theme.AIHubTheme

class MainActivity : ComponentActivity() {
    private lateinit var pillManager: VGHPillManager
    private lateinit var db: VGHDatabase

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Settings.canDrawOverlays(this)) {
            // Permission granted, we can show the pill later
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        pillManager = VGHPillManager(this)
        db = Room.databaseBuilder(
            applicationContext,
            VGHDatabase::class.java, "vgh-database"
        ).build()
        
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            overlayPermissionLauncher.launch(intent)
        }

        setContent {
            AIHubTheme {
                MainScreen(pillManager, db)
            }
        }
    }
}

sealed class Screen {
    object Main : Screen()
    object Prompts : Screen()
    object Notes : Screen()
    object Bridge : Screen()
    object Settings : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(pillManager: VGHPillManager, db: VGHDatabase) {
    val assistants = defaultAssistants
    var selectedAssistant by remember { mutableStateOf(assistants[0]) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
    var showGridMenu by remember { mutableStateOf(false) }

    val prompts by db.vghDao().getAllPrompts().collectAsState(initial = emptyList())
    val notes by db.vghDao().getAllNotes().collectAsState(initial = emptyList())

    LaunchedEffect(selectedAssistant, currentScreen) {
        if (currentScreen == Screen.Main) {
            pillManager.showPill(selectedAssistant.name) {
                showGridMenu = true
            }
        } else {
            pillManager.hidePill()
        }
    }

    when (currentScreen) {
        Screen.Main -> {
            MainContent(
                selectedAssistant = selectedAssistant,
                drawerState = drawerState,
                assistants = assistants,
                onAssistantSelected = { selectedAssistant = it },
                onOpenMenu = { scope.launch { drawerState.open() } }
            )
        }
        Screen.Prompts -> {
            com.foss.viangreenhouse.ui.VGHPromptsScreen(
                prompts = prompts,
                onAddPrompt = { name, content ->
                    scope.launch {
                        db.vghDao().insertPrompt(VGHQuickPrompt(UUID.randomUUID().toString(), name, content, prompts.size))
                    }
                },
                onDeletePrompt = { prompt ->
                    scope.launch {
                        db.vghDao().deletePrompt(prompt)
                    }
                },
                onBack = { currentScreen = Screen.Main }
            )
        }
        Screen.Notes -> {
            com.foss.viangreenhouse.ui.VGHNotesScreen(
                notes = notes,
                onAddNote = { title, content ->
                    scope.launch {
                        val now = System.currentTimeMillis()
                        db.vghDao().insertNote(
                            com.foss.viangreenhouse.db.VGHNote(
                                UUID.randomUUID().toString(),
                                title,
                                content,
                                now,
                                now
                            )
                        )
                    }
                },
                onDeleteNote = { note ->
                    scope.launch {
                        db.vghDao().deleteNote(note)
                    }
                },
                onBack = { currentScreen = Screen.Main }
            )
        }
        Screen.Bridge -> {
            com.foss.viangreenhouse.ui.VGHBridgeScreen(
                onBack = { currentScreen = Screen.Main }
            )
        }
        Screen.Settings -> {
            com.foss.viangreenhouse.ui.VGHSettingsScreen(
                onBack = { currentScreen = Screen.Main }
            )
        }
        else -> { /* Other screens */ }
    }

    if (showGridMenu) {
        com.foss.viangreenhouse.ui.VGHGridMenu(
            onDismiss = { showGridMenu = false },
            onNavigateToNotes = { currentScreen = Screen.Notes; showGridMenu = false },
            onNavigateToPrompts = { currentScreen = Screen.Prompts; showGridMenu = false },
            onNavigateToBridge = { currentScreen = Screen.Bridge; showGridMenu = false },
            onNavigateToSettings = { currentScreen = Screen.Settings; showGridMenu = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    selectedAssistant: Assistant,
    drawerState: DrawerState,
    assistants: List<Assistant>,
    onAssistantSelected: (Assistant) -> Unit,
    onOpenMenu: () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Vian Greenhouse",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                HorizontalDivider()
                assistants.forEach { assistant ->
                    NavigationDrawerItem(
                        label = { Text(assistant.name) },
                        selected = assistant == selectedAssistant,
                        onClick = {
                            onAssistantSelected(assistant)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        icon = { Text(assistant.icon) }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = { Text(selectedAssistant.name) },
                        navigationIcon = {
                            IconButton(onClick = onOpenMenu) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { /* Refresh logic would go here */ }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                            }
                        }
                    )
                    if (selectedAssistant.tabType == TabType.RESEARCH) {
                        ResearchUrlBar(
                            currentUrl = selectedAssistant.url,
                            onUrlSubmit = { newUrl ->
                                // In a real app, we'd update the assistant's URL or state
                                // For now, we'll just trigger a reload in the WebView
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                AIWebView(url = selectedAssistant.url)
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AIWebView(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = { webView ->
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ResearchUrlBar(currentUrl: String, onUrlSubmit: (String) -> Unit) {
    var text by remember { mutableStateOf(currentUrl) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = {
                    onUrlSubmit(text)
                    focusManager.clearFocus()
                })
            )
        }
        if (text.isEmpty()) {
            Text(
                "Search or enter URL",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 26.dp)
            )
        }
    }
}


