package com.tadex.dramastudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF101116)
private val Panel = Color(0xFF1D2029)
private val Muted = Color(0xFF9CA3B5)
private val Purple = Color(0xFF9B8CFF)
private val Cyan = Color(0xFF69D7F5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TadexApp() }
    }
}

@Composable
fun TadexApp() {
    var tab by remember { mutableStateOf("Home") }
    var selectedTool by remember { mutableStateOf("AI Drama") }
    var prompt by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("Romance") }
    var aspect by remember { mutableStateOf("9:16 Vertical") }
    var notice by remember { mutableStateOf("") }
    var showSettings by remember { mutableStateOf(false) }
    var apiUrl by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    val works = remember { mutableStateListOf("The Billionaire's Secret • Drama script", "Midnight Promise • Video idea") }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg, surface = Panel, primary = Purple,
            secondary = Cyan, onBackground = Color.White, onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF191B22), contentColor = Color.White) {
                    listOf("Home", "Tools", "Works").forEach { item ->
                        NavigationBarItem(
                            selected = tab == item,
                            onClick = { tab = item; notice = "" },
                            icon = {
                                Icon(
                                    when (item) {
                                        "Home" -> Icons.Default.Home
                                        "Tools" -> Icons.Default.AutoAwesome
                                        else -> Icons.Default.VideoLibrary
                                    }, contentDescription = item
                                )
                            },
                            label = { Text(item) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color(0xFF393448),
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when {
                showSettings -> SettingsScreen(
                    apiUrl, { apiUrl = it }, apiKey, { apiKey = it },
                    onBack = { showSettings = false }
                )
                tab == "Home" -> HomeScreen(
                    padding = padding,
                    onTool = { selectedTool = it; tab = "Tools" },
                    onSettings = { showSettings = true },
                    works = works
                )
                tab == "Tools" -> ToolsScreen(
                    padding, selectedTool, { selectedTool = it },
                    prompt, { prompt = it }, genre, { genre = it },
                    aspect, { aspect = it },
                    onGenerate = {
                        if (prompt.isBlank()) notice = "Write a prompt first."
                        else {
                            works.add(0, "$genre • $selectedTool • ${prompt.take(34)}")
                            notice = "Project saved! Connect an AI provider in Settings to enable real generation."
                        }
                    },
                    notice
                )
                else -> WorksScreen(padding, works) { works.remove(it) }
            }
        }
    }
}

@Composable
fun HomeScreen(
    padding: PaddingValues,
    onTool: (String) -> Unit,
    onSettings: () -> Unit,
    works: List<String>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(42.dp).background(
                        Brush.linearGradient(listOf(Purple, Cyan)), CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) { Text("T", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 23.sp) }
                Text("Tadex", modifier = Modifier.padding(start = 10.dp).weight(1f),
                    fontSize = 23.sp, fontWeight = FontWeight.Bold)
                Text("PRO", color = Color(0xFFFFD27A), fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(Panel, RoundedCornerShape(20.dp)).clickable { onSettings() }
                        .padding(horizontal = 14.dp, vertical = 9.dp))
                IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, "Settings", tint = Muted) }
            }
        }
        item {
            Box(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .height(300.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF203D64), Color(0xFF29213E), Color(0xFF101116))
                        ), RoundedCornerShape(28.dp)
                    )
                    .padding(22.dp)
            ) {
                Column(Modifier.align(Alignment.TopStart)) {
                    Text("YOUR AI STORY STUDIO", color = Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(18.dp))
                    Text("Good morning, Director", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
                    Text("What story are we making today?", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(18.dp))
                    Text("Turn your imagination into cinematic stories.",
                        color = Color(0xFFD5D9E6), fontSize = 14.sp)
                }
                Box(
                    Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color.White, Color(0xFFE9E4FF))),
                            RoundedCornerShape(30.dp))
                        .clickable { onTool("AI Drama") }.padding(17.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF252337))
                        Text("  Start creating short dramas", color = Color(0xFF171722),
                            fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ArrowForward, null, tint = Color.Black)
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                ToolShortcut("AI Drama", Icons.Default.Movie) { onTool("AI Drama") }
                ToolShortcut("AI Video", Icons.Default.Videocam) { onTool("AI Video") }
                ToolShortcut("AI Photo", Icons.Default.Image) { onTool("AI Photo") }
                ToolShortcut("AI Effects", Icons.Default.AutoFixHigh) { onTool("AI Effects") }
            }
        }
        item { SectionTitle("Explore creative tools") }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                listOf(
                    Triple("AI Voice", "Create narration and character dialogue", Icons.Default.RecordVoiceOver),
                    Triple("AI Story Writer", "Build plots, scenes and dialogue", Icons.Default.EditNote),
                    Triple("AI Poster Maker", "Design drama covers and thumbnails", Icons.Default.Palette),
                    Triple("AI Photo Animator", "Bring still images to life", Icons.Default.Animation)
                ).forEach { (name, desc, icon) ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 5.dp)
                            .background(Panel, RoundedCornerShape(16.dp))
                            .clickable { onTool(name) }.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(icon, null, tint = Purple, modifier = Modifier.size(28.dp))
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text(desc, color = Muted, fontSize = 12.sp)
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Muted)
                    }
                }
            }
        }
        item { SectionTitle("Drama inspiration") }
        item {
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InspirationCard("THE DELIVERYMAN'S SECRET", "A hidden fortune. One shocking wedding.", Modifier.weight(1f))
                InspirationCard("THE WRONG DOOR", "One choice changes everything.", Modifier.weight(1f))
            }
        }
        item { SectionTitle("Recent works") }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                works.take(2).forEach { Text("✦  $it", color = Muted, modifier = Modifier.padding(vertical = 6.dp)) }
            }
        }
    }
}

@Composable
fun ToolShortcut(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(78.dp).clickable { onClick() }) {
        Box(Modifier.size(62.dp).background(Panel, RoundedCornerShape(19.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Color(0xFFD9D4FF), modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.height(7.dp))
        Text(label, color = Muted, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, top = 18.dp, bottom = 12.dp))
}

@Composable
fun InspirationCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier.background(
        Brush.verticalGradient(listOf(Color(0xFF343044), Color(0xFF1D2029))),
        RoundedCornerShape(18.dp)
    ).padding(14.dp).height(125.dp), verticalArrangement = Arrangement.Bottom) {
        Text("✦  FEATURED STORY", color = Cyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        Text(subtitle, color = Muted, fontSize = 11.sp)
    }
}

@Composable
fun ToolsScreen(
    padding: PaddingValues, selected: String, onSelected: (String) -> Unit,
    prompt: String, onPrompt: (String) -> Unit,
    genre: String, onGenre: (String) -> Unit,
    aspect: String, onAspect: (String) -> Unit,
    onGenerate: () -> Unit, notice: String
) {
    val tools = listOf("AI Drama", "AI Video", "AI Photo", "AI Effects", "AI Voice", "AI Story Writer", "AI Poster Maker", "AI Photo Animator")
    LazyColumn(
        Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Text("Creative studio", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
            Text("Choose a tool and describe your idea.", color = Muted)
            Spacer(Modifier.height(18.dp))
        }
        item {
            Text("SELECT TOOL", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tools.chunked(2).forEach { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        pair.forEach { tool ->
                            val active = selected == tool
                            Text(tool, modifier = Modifier.weight(1f)
                                .background(if (active) Color(0xFF39334F) else Panel, RoundedCornerShape(12.dp))
                                .clickable { onSelected(tool) }.padding(12.dp),
                                color = if (active) Color.White else Muted, fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp, textAlign = TextAlign.Center)
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
            Spacer(Modifier.height(22.dp))
            Text("YOUR IDEA / PROMPT", fontSize = 11.sp, color = Muted, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = prompt, onValueChange = onPrompt,
                modifier = Modifier.fillMaxWidth().height(155.dp),
                placeholder = { Text("Example: A poor deliveryman discovers he is the secret owner of a billion-dollar company...") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple, unfocusedBorderColor = Color(0xFF454957),
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.height(18.dp))
            Text("GENRE", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(genre); Icon(Icons.Default.ArrowDropDown, null)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Romance", "Action", "Comedy", "Thriller", "Drama", "Fantasy", "Horror").forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = { onGenre(it); expanded = false })
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("FORMAT", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                listOf("9:16 Vertical", "16:9 Landscape", "1:1 Square").forEach {
                    FilterChip(selected = aspect == it, onClick = { onAspect(it) }, label = { Text(it, fontSize = 10.sp) })
                }
            }
            Spacer(Modifier.height(22.dp))
            Button(
                onClick = onGenerate, modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple, contentColor = Color(0xFF171522))
            ) {
                Icon(Icons.Default.AutoAwesome, null)
                Text("  Create with AI", fontWeight = FontWeight.Bold)
            }
            if (notice.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(notice, color = Cyan, fontSize = 13.sp)
            }
            Spacer(Modifier.height(16.dp))
            Text("Note: This starter app saves your prompts and projects. Connect a compatible AI generation API in Settings to produce actual media.",
                color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun WorksScreen(padding: PaddingValues, works: List<String>, onDelete: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp)) {
        item {
            Text("My works", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("Your saved ideas and creative projects", color = Muted)
            Spacer(Modifier.height(20.dp))
        }
        if (works.isEmpty()) {
            item { Text("No projects yet. Start creating your first story.", color = Muted) }
        }
        items(works) { work ->
            Row(
                Modifier.fillMaxWidth().padding(bottom = 10.dp)
                    .background(Panel, RoundedCornerShape(15.dp)).padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Movie, null, tint = Purple)
                Text(work, Modifier.weight(1f).padding(horizontal = 12.dp), fontSize = 13.sp)
                IconButton(onClick = { onDelete(work) }) { Icon(Icons.Default.DeleteOutline, null, tint = Muted) }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    apiUrl: String, onUrl: (String) -> Unit,
    apiKey: String, onKey: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
            Text("Settings", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Text("AI provider connection", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Enter an API endpoint and key from your chosen AI provider. Different providers use different API formats; this starter build does not automatically support every provider.",
            color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(value = apiUrl, onValueChange = onUrl, label = { Text("API base URL") },
            placeholder = { Text("https://your-provider.example/api") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = apiKey, onValueChange = onKey, label = { Text("API key") },
            placeholder = { Text("Paste your private API key") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(14.dp))
        Text("Security: Do not share your API key. For a production app, store provider credentials on a secure backend rather than directly in the mobile app.",
            color = Color(0xFFFFC878), fontSize = 12.sp)
        Spacer(Modifier.height(20.dp))
        Text("App version 1.0.0 • Tadex Tech", color = Muted, fontSize = 12.sp)
    }
}
