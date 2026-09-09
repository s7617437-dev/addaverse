package com.voiceconnect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF071329)
private val Navy2 = Color(0xFF0D1C3A)
private val Purple = Color(0xFF8A4DFF)
private val Pink = Color(0xFFFF3FB4)
private val Cyan = Color(0xFF22D7FF)
private val Gold = Color(0xFFFFC62E)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VoiceConnectApp() }
    }
}

@Composable
fun VoiceConnectApp() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Navy,
            surface = Navy2,
            primary = Purple,
            secondary = Cyan
        )
    ) {
        PartyHome()
    }
}

data class Room(
    val title: String,
    val subtitle: String,
    val country: String,
    val users: String,
    val rating: String,
    val host: String,
    val badge: String,
    val gradient: List<Color>
)

private val rooms = listOf(
    Room("রাত জাগা আড্ডা 🔥", "Talk • Music • Friends", "🇧🇩 BD", "32/50", "4.8", "Host", "HOT",
        listOf(Color(0xFF5B0B78), Color(0xFF151B54))),
    Room("Gaming & Chill 🎮", "Games • Fun • Friends", "🇧🇩 BD", "45/60", "4.9", "Host", "LIVE",
        listOf(Color(0xFF053C5E), Color(0xFF17105C))),
    Room("Music Room", "Sing • Music • Vibes", "🇮🇳 IN", "20/35", "4.7", "Host", "MUSIC",
        listOf(Color(0xFF7A0B78), Color(0xFF20205D))),
    Room("Friendship Forever", "Talk • Laugh • Make Friends", "🇵🇰 PK", "18/30", "4.6", "Host", "NEW",
        listOf(Color(0xFF0A4E7A), Color(0xFF31135F)))
)

@Composable
fun PartyHome() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Navy,
        bottomBar = { BottomBar(selectedTab) { selectedTab = it } }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Header()
            Spacer(Modifier.height(16.dp))
            TopTabs()
            Spacer(Modifier.height(14.dp))
            HeroBanner()
            Spacer(Modifier.height(14.dp))
            RankingRow()
            Spacer(Modifier.height(12.dp))
            PrizePool()
            Spacer(Modifier.height(14.dp))
            CountryFilters()
            Spacer(Modifier.height(14.dp))
            Text("Live Voice Rooms", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            rooms.chunked(2).forEach { row ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { room ->
                        RoomCard(room, Modifier.weight(1f))
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
            }
            GameRow()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Cyan, Purple, Pink))),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Mic, null, tint = Color.White, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "VoiceConnect",
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text("Talk  •  Make Friends  •  Play Together", fontSize = 11.sp, color = Color.LightGray)
        }
        IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search", tint = Color.White) }
        IconButton(onClick = {}) { Icon(Icons.Default.Notifications, "Notifications", tint = Color.White) }
        Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFF172B52)) {
            Row(Modifier.padding(horizontal = 9.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🪙", fontSize = 15.sp)
                Spacer(Modifier.width(3.dp))
                Text("50", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(4.dp))
                Text("+", color = Cyan, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TopTabs() {
    val tabs = listOf("♛ Mine", "▣ Party", "♛ Events", "🎮 Games")
    Row(
        Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            Surface(
                color = if (index == 0) Purple else Color.Transparent,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.clickable {}
            ) {
                Text(
                    tab,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF6519D8), Color(0xFFFF2BAF), Color(0xFF172EAA))
                )
            )
            .padding(18.dp)
    ) {
        Column(Modifier.align(Alignment.CenterStart)) {
            Text("👑  VoiceConnect", color = Gold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Live Voice Party", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text("Meet • Talk • Share • Play", color = Color.White.copy(.85f))
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Purple)
            ) { Text("Join Now →", fontWeight = FontWeight.Bold) }
        }
        Text("🎧", fontSize = 62.sp, modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
fun RankingRow() {
    val items = listOf(
        "Charm Ranking" to "👑",
        "CP Ranking" to "🏆",
        "Family" to "🪽",
        "Hall of Fame" to "⭐"
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { (title, icon) ->
            Surface(
                Modifier.weight(1f).height(105.dp),
                shape = RoundedCornerShape(17.dp),
                color = Color(0xFF281A50),
                tonalElevation = 5.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(icon, fontSize = 26.sp)
                    Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("TOP 1", color = Gold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun PrizePool() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            Modifier.weight(1.2f).height(72.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF3B173D)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.Center) {
                Text("🏆  Prize Pool", color = Color.White, fontWeight = FontWeight.Bold)
                Text("🪙 1,250,000", color = Gold, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
        Surface(
            Modifier.weight(1f).height(72.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF4020A0)
        ) {
            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.Center) {
                Text("📅 Next Event", color = Color.White.copy(.8f))
                Text("03 : 12 : 45", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CountryFilters() {
    val countries = listOf("🔥 Popular", "🇧🇩 Bangladesh", "🇮🇳 India", "🇵🇰 Pakistan", "🇳🇵 Nepal", "🇮🇩 Indonesia")
    Row(
        Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        countries.forEachIndexed { index, name ->
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = if (index == 0) Gold else Color(0xFF13284B)
            ) {
                Text(
                    name,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    color = if (index == 0) Color(0xFF382700) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun RoomCard(room: Room, modifier: Modifier) {
    Surface(
        modifier = modifier
            .height(190.dp)
            .clickable {},
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(room.gradient))
                .padding(11.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (room.badge == "LIVE") Color(0xFF16C784) else Color(0xFFFF3B5C),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text("● ${room.badge}", Modifier.padding(horizontal = 8.dp, vertical = 5.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Box(
                Modifier
                    .size(58.dp)
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Pink, Purple))),
                contentAlignment = Alignment.Center
            ) {
                Text("🎧", fontSize = 27.sp)
            }

            Column(
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 68.dp, top = 4.dp, end = 6.dp)
            ) {
                Text(room.title, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(room.subtitle, fontSize = 10.sp, color = Color.White.copy(.72f))
                Spacer(Modifier.height(8.dp))
                Text("👑 ${room.host}", fontSize = 10.sp)
            }

            Column(Modifier.align(Alignment.BottomStart)) {
                Text("${room.country}   👥 ${room.users}", fontSize = 11.sp)
                Text("★ ${room.rating}", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            FloatingActionButton(
                onClick = {},
                modifier = Modifier.size(45.dp).align(Alignment.BottomEnd),
                containerColor = Purple,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Mic, "Join room", modifier = Modifier.size(21.dp))
            }
        }
    }
}

@Composable
fun GameRow() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        listOf(
            "🎲 LUDO\nPlay Now →",
            "🎮 Game Center\nMore Games →",
            "🏆 SURVIVOR\nJoin Now →"
        ).forEach { title ->
            Surface(
                Modifier.weight(1f).height(90.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF103B5C)
            ) {
                Box(Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                    Text(title, fontWeight = FontWeight.ExtraBold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun BottomBar(selected: Int, onSelect: (Int) -> Unit) {
    NavigationBar(containerColor = Color(0xFF08162F)) {
        val items = listOf(
            Icons.Default.Home to "Party",
            Icons.Default.Explore to "Discover",
            Icons.Default.Chat to "Message",
            Icons.Default.Person to "My"
        )
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selected == index,
                onClick = { onSelect(index) },
                icon = { Icon(item.first, item.second) },
                label = { Text(item.second) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Purple,
                    indicatorColor = Purple.copy(alpha = .18f),
                    unselectedIconColor = Color(0xFF9CA9C7),
                    unselectedTextColor = Color(0xFF9CA9C7)
                )
            )
        }
    }
}
