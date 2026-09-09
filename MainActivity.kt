package com.voiceconnect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Teal = Color(0xFF09C9B8)
private val TealDark = Color(0xFF006B67)
private val Gold = Color(0xFFFFC62E)
private val Navy = Color(0xFF071329)
private val Card = Color(0xFF102447)
private val Purple = Color(0xFF8B4DFF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AddaVerseApp() }
    }
}

enum class Screen { PARTY, DISCOVER, MESSAGE, MY, ROOM, SHOP }

data class Room(
    val title: String, val country: String, val users: String,
    val subtitle: String, val gradient: List<Color>
)

data class Gift(val name: String, val price: Int, val emoji: String, val days: String = "7 day")

val rooms = listOf(
    Room("রাত জাগা আড্ডা 🔥", "🇧🇩", "32/50", "Talk • Music • Friends", listOf(Color(0xFF5B0B78), Color(0xFF171B54))),
    Room("Gaming & Chill 🎮", "🇧🇩", "45/60", "Games • Fun • Friends", listOf(Color(0xFF053C5E), Color(0xFF17105C))),
    Room("Music Room 🎵", "🇮🇳", "20/35", "Sing • Music • Vibes", listOf(Color(0xFF7A0B78), Color(0xFF20205D))),
    Room("Friendship Forever", "🇵🇰", "18/30", "Talk • Laugh • Friends", listOf(Color(0xFF0A4E7A), Color(0xFF31135F)))
)

val gifts = listOf(
    Gift("Blueberry", 1000, "🫐"),
    Gift("Golden Car", 30000, "🚗"),
    Gift("Royal Bird", 210000, "🦅"),
    Gift("Golden Jeep", 60000, "🚙"),
    Gift("Ruby Throne", 100000, "👑"),
    Gift("Tiger", 60000, "🐯"),
    Gift("Royal Entrance", 100000, "✨"),
    Gift("Yacht", 10000, "🛥️")
)

@Composable
fun AddaVerseApp() {
    var screen by remember { mutableStateOf(Screen.PARTY) }
    var selectedRoom by remember { mutableStateOf(rooms.first()) }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Teal, secondary = Gold, background = Navy, surface = Card
    )) {
        when (screen) {
            Screen.PARTY -> PartyScreen(
                onRoom = { selectedRoom = it; screen = Screen.ROOM },
                onTab = { screen = it }
            )
            Screen.DISCOVER -> DiscoverScreen { screen = Screen.ROOM }
            Screen.MESSAGE -> MessageScreen()
            Screen.MY -> MyScreen(onShop = { screen = Screen.SHOP })
            Screen.ROOM -> RoomScreen(selectedRoom) { screen = Screen.PARTY }
            Screen.SHOP -> ShopScreen { screen = Screen.MY }
        }
    }
}

@Composable
fun BottomBar(current: Screen, onTab: (Screen) -> Unit) {
    NavigationBar(containerColor = Color(0xFF063B3A)) {
        val items = listOf(
            Screen.PARTY to (Icons.Default.Home to "Party"),
            Screen.DISCOVER to (Icons.Default.Explore to "Discover"),
            Screen.MESSAGE to (Icons.Default.Chat to "Message"),
            Screen.MY to (Icons.Default.Person to "My")
        )
        items.forEach { (s, pair) ->
            NavigationBarItem(
                selected = current == s,
                onClick = { onTab(s) },
                icon = { Icon(pair.first, null) },
                label = { Text(pair.second) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Teal, selectedTextColor = Teal,
                    unselectedIconColor = Color.LightGray, unselectedTextColor = Color.LightGray,
                    indicatorColor = Teal.copy(alpha = .15f)
                )
            )
        }
    }
}

@Composable
fun PartyScreen(onRoom: (Room) -> Unit, onTab: (Screen) -> Unit) {
    Scaffold(
        containerColor = Navy,
        bottomBar = { BottomBar(Screen.PARTY, onTab) }
    ) { pad ->
        Column(
            Modifier.fillMaxSize().padding(pad).verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            Header()
            Spacer(Modifier.height(10.dp))
            CategoryTabs()
            Spacer(Modifier.height(10.dp))
            Hero()
            Spacer(Modifier.height(12.dp))
            RankingCards()
            Spacer(Modifier.height(10.dp))
            PrizePool()
            Spacer(Modifier.height(10.dp))
            CountryFilters()
            Spacer(Modifier.height(14.dp))
            Text("🔥 Popular Voice Rooms", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            rooms.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { r -> RoomCard(r, Modifier.weight(1f)) { onRoom(r) } }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
            }
            GameCards()
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun Header() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(48.dp).clip(CircleShape)
                .background(Brush.linearGradient(listOf(Teal, Purple, Gold))),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Default.Mic, null, tint = Color.White) }
        Spacer(Modifier.width(9.dp))
        Column(Modifier.weight(1f)) {
            Text("AddaVerse", fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text("Voice • Friends • Party", fontSize = 11.sp, color = Color.LightGray)
        }
        IconButton({}) { Icon(Icons.Default.Search, "Search") }
        IconButton({}) { Icon(Icons.Default.Notifications, "Notifications") }
    }
}

@Composable
fun CategoryTabs() {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Mine", "Party", "Events", "Games").forEachIndexed { i, t ->
            Surface(shape = RoundedCornerShape(22.dp), color = if (i == 1) Teal else Card) {
                Text(t, Modifier.padding(horizontal = 17.dp, vertical = 9.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun Hero() {
    Box(
        Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF087F7A), Color(0xFF3D1A87), Color(0xFFB0147B))))
            .padding(17.dp)
    ) {
        Column(Modifier.align(Alignment.CenterStart)) {
            Text("👑 FEATURED PARTY", color = Gold, fontWeight = FontWeight.Bold)
            Text("Eternal Voice Night", fontSize = 25.sp, fontWeight = FontWeight.Black)
            Text("Meet new people • Talk • Have fun", color = Color.White.copy(.85f))
            Spacer(Modifier.height(8.dp))
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color(0xFF3B2900))) {
                Text("Join Now", fontWeight = FontWeight.Bold)
            }
        }
        Text("🎧✨", Modifier.align(Alignment.BottomEnd), fontSize = 44.sp)
    }
}

@Composable
fun RankingCards() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        listOf("👑\nCharm", "🏆\nCP", "🪽\nFamily", "⭐\nFame").forEach { t ->
            Surface(Modifier.weight(1f).height(92.dp), RoundedCornerShape(15.dp), color = Color(0xFF24415A)) {
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    Text(t, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Text("TOP 1", color = Gold, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun PrizePool() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(Modifier.weight(1.2f).height(62.dp), RoundedCornerShape(15.dp), color = Color(0xFF44203C)) {
            Column(Modifier.padding(9.dp), Arrangement.Center) {
                Text("🏆 Prize Pool", fontWeight = FontWeight.Bold)
                Text("🪙 1,250,000", color = Gold, fontWeight = FontWeight.Black, fontSize = 17.sp)
            }
        }
        Surface(Modifier.weight(1f).height(62.dp), RoundedCornerShape(15.dp), color = Color(0xFF4020A0)) {
            Column(Modifier.padding(9.dp), Arrangement.Center) {
                Text("Next Event", fontSize = 11.sp)
                Text("03:12:45", fontWeight = FontWeight.Black, fontSize = 17.sp)
            }
        }
    }
}

@Composable
fun CountryFilters() {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        listOf("🔥 Popular", "🇧🇩 Bangladesh", "🇮🇳 India", "🇵🇰 Pakistan", "🇳🇵 Nepal").forEachIndexed { i, t ->
            Surface(RoundedCornerShape(20.dp), color = if (i == 0) Gold else Color(0xFF17304E)) {
                Text(t, Modifier.padding(horizontal = 13.dp, vertical = 9.dp),
                    color = if (i == 0) Color(0xFF3A2800) else Color.White,
                    fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RoomCard(room: Room, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.height(185.dp).clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(room.gradient)).clickable(onClick = onClick)
            .padding(11.dp)
    ) {
        Surface(RoundedCornerShape(10.dp), color = Color(0xFFE72B61), Modifier.align(Alignment.TopStart)) {
            Text("● LIVE", Modifier.padding(horizontal = 7.dp, vertical = 4.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Box(Modifier.size(58.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Teal, Purple)))
            .align(Alignment.CenterStart), contentAlignment = Alignment.Center) { Text("🎧", fontSize = 27.sp) }
        Column(Modifier.align(Alignment.CenterStart).padding(start = 68.dp, end = 4.dp)) {
            Text(room.title, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(room.subtitle, fontSize = 10.sp, color = Color.White.copy(.75f))
            Spacer(Modifier.height(7.dp))
            Text("👑 Host", fontSize = 10.sp)
        }
        Column(Modifier.align(Alignment.BottomStart)) {
            Text("${room.country}  👥 ${room.users}", fontSize = 11.sp)
            Text("★ 4.8", color = Gold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
        FloatingActionButton({ onClick() }, Modifier.size(42.dp).align(Alignment.BottomEnd), containerColor = Teal) {
            Icon(Icons.Default.Mic, null)
        }
    }
}

@Composable
fun GameCards() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("🎲 LUDO\n2144 players", "🎮 Game Center\nPlay now", "🏆 SURVIVOR\nJoin event").forEach {
            Surface(Modifier.weight(1f).height(82.dp), RoundedCornerShape(15.dp), color = Color(0xFF16465D)) {
                Box(contentAlignment = Alignment.Center) { Text(it, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun RoomScreen(room: Room, onBack: () -> Unit) {
    Scaffold(containerColor = Color(0xFF1D1207)) { pad ->
        Box(Modifier.fillMaxSize().padding(pad)) {
            Column(Modifier.fillMaxSize().padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                    Column(Modifier.weight(1f)) {
                        Text(room.title, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        Text("${room.country}  •  ${room.users} listeners", fontSize = 11.sp, color = Color.LightGray)
                    }
                    IconButton({}) { Icon(Icons.Default.Share, "Share") }
                    IconButton({}) { Icon(Icons.Default.MoreVert, "More") }
                }
                Spacer(Modifier.height(10.dp))
                Surface(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), color = Color(0xFF3B260E)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("🎙️ LIVE VOICE PARTY", color = Gold, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Host", fontWeight = FontWeight.Bold)
                        SeatGrid()
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text("All   Chat", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(6.dp))
                listOf("🌸 Welcome to the room!", "🎧 Music starts soon", "💬 Say hello to everyone").forEach {
                    Text(it, Modifier.padding(vertical = 5.dp), color = Color.White.copy(.85f))
                }
                Spacer(Modifier.weight(1f))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    RoundAction(Icons.Default.Chat, "Chat")
                    RoundAction(Icons.Default.MicOff, "Mute")
                    RoundAction(Icons.Default.Face, "React")
                    RoundAction(Icons.Default.CardGiftcard, "Gift", Teal)
                    RoundAction(Icons.Default.VideogameAsset, "Game")
                    RoundAction(Icons.Default.ExitToApp, "Leave", Color(0xFFE24C61))
                }
            }
        }
    }
}

@Composable
fun SeatGrid() {
    val seats = (1..15).toList()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(10.dp))
        Seat("👑", "Host")
        Spacer(Modifier.height(10.dp))
        seats.chunked(5).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { n -> Seat("💺", "No.$n") }
            }
            Spacer(Modifier.height(9.dp))
        }
    }
}

@Composable
fun Seat(icon: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(54.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Gold, Teal))),
            contentAlignment = Alignment.Center) { Text(icon, fontSize = 26.sp) }
        Text(label, fontSize = 10.sp)
        Text("💗0", fontSize = 9.sp, color = Color.LightGray)
    }
}

@Composable
fun RoundAction(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color = Color(0xFF28333A)) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(Modifier.size(50.dp), CircleShape, color = color) { Box(contentAlignment = Alignment.Center) { Icon(icon, null) } }
        Text(text, fontSize = 9.sp, color = Color.White.copy(.75f))
    }
}

@Composable
fun ShopScreen(onBack: () -> Unit) {
    Scaffold(containerColor = Color(0xFF006B67), bottomBar = {
        Row(Modifier.fillMaxWidth().background(Color(0xFF004E4B)).padding(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text("🪙 11", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Teal)) { Text("Send") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color.Black)) { Text("Purchase") }
        }
    }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad)) {
            Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                Text("Gift Shop", fontSize = 22.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                Text("🪙 11", color = Gold, fontWeight = FontWeight.Bold)
            }
            Row(Modifier.fillMaxSize()) {
                Column(Modifier.width(100.dp).verticalScroll(rememberScrollState()).padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("🚗 Car", "🖼️ Frame", "🆔 Unique ID", "💍 Ring", "🌟 Profile", "✨ Entrance").forEach {
                        Surface(RoundedCornerShape(15.dp), color = Color(0xFF087D77)) {
                            Text(it, Modifier.padding(10.dp), textAlign = TextAlign.Center, fontSize = 11.sp)
                        }
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(5.dp),
                    contentPadding = PaddingValues(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(gifts) { gift ->
                        Surface(RoundedCornerShape(16.dp), color = Color(0xFF0A8B83)) {
                            Column(Modifier.padding(9.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(Modifier.fillMaxWidth()) {
                                    Text(gift.days, fontSize = 10.sp)
                                    Spacer(Modifier.weight(1f))
                                    Icon(Icons.Default.PlayCircle, null, Modifier.size(17.dp))
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(gift.emoji, fontSize = 50.sp)
                                Text(gift.name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("🪙 ${gift.price}", color = Gold, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyScreen(onShop: () -> Unit) {
    Scaffold(containerColor = Color(0xFFE8FAF8), bottomBar = { BottomBar(Screen.MY) {} }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).verticalScroll(rememberScrollState())) {
            Box(Modifier.fillMaxWidth().height(245.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF08B9B0), Color(0xFFE8FAF8))))) {
                Column(Modifier.fillMaxSize().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.size(78.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Gold, Purple))),
                        contentAlignment = Alignment.Center) { Text("S", fontSize = 38.sp, fontWeight = FontWeight.Black) }
                    Text("Sifat", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 23.sp)
                    Text("UID: 1136496", color = Color.DarkGray)
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Stat("2", "Follow"); Stat("135", "Fans"); Stat("3.5M", "Charm")
                    }
                }
            }
            Column(Modifier.padding(14.dp)) {
                Surface(Modifier.fillMaxWidth().height(90.dp), RoundedCornerShape(17.dp), color = Gold) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("💰", fontSize = 38.sp); Spacer(Modifier.width(12.dp))
                        Column { Text("Wallet", fontSize = 24.sp, fontWeight = FontWeight.Black); Text("🪙 11 coins", color = Color(0xFF513A00)) }
                        Spacer(Modifier.weight(1f)); Icon(Icons.Default.ChevronRight, null)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBox("VIP", "Not obtained", Modifier.weight(1f))
                    InfoBox("Wealth level", "Lv.20", Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                listOf(
                    "💰 Invite new users to earn coins" to false,
                    "🏅 Medal of Honor" to false,
                    "🛠️ Customization Center" to false,
                    "🛍️ Shop" to true,
                    "🧩 Props" to false,
                    "📋 Task" to false,
                    "📊 Data Center" to false,
                    "👥 Agency Center" to false,
                    "🏆 BD Center" to false,
                    "🏠 Family" to false,
                    "💚 CP Nest" to false,
                    "📒 Reward Records" to false,
                    "💬 Feedback" to false,
                    "⚙️ Setting" to false
                ).forEach { (label, shop) ->
                    Surface(Modifier.fillMaxWidth().clickable { if (shop) onShop() },
                        color = Color.White, shape = RoundedCornerShape(12.dp)) {
                        Row(Modifier.padding(horizontal = 14.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(label, Modifier.weight(1f), color = Color(0xFF202020), fontSize = 16.sp)
                            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Text(label, color = Color.DarkGray)
    }
}

@Composable
fun InfoBox(title: String, value: String, modifier: Modifier) {
    Surface(modifier.height(78.dp), RoundedCornerShape(14.dp), color = Color(0xFF5B55D9)) {
        Column(Modifier.padding(10.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White.copy(.8f), fontSize = 12.sp)
        }
    }
}

@Composable
fun DiscoverScreen(onRoom: () -> Unit) {
    Scaffold(containerColor = Navy, bottomBar = { BottomBar(Screen.DISCOVER) {} }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(14.dp)) {
            Text("Discover", fontSize = 28.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(10.dp))
            Text("Find rooms and people", color = Color.LightGray)
            Spacer(Modifier.height(18.dp))
            rooms.forEach { RoomCard(it, Modifier.fillMaxWidth(), onRoom); Spacer(Modifier.height(10.dp)) }
        }
    }
}

@Composable
fun MessageScreen() {
    Scaffold(containerColor = Navy, bottomBar = { BottomBar(Screen.MESSAGE) {} }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(14.dp)) {
            Text("Messages", fontSize = 28.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))
            listOf("AddaVerse Team", "Room Friends", "New Friends").forEach { name ->
                Surface(Modifier.fillMaxWidth().padding(vertical = 5.dp), RoundedCornerShape(16.dp), color = Card) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(48.dp).clip(CircleShape).background(Teal), contentAlignment = Alignment.Center) { Text("💬") }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text("New message • Tap to open", fontSize = 11.sp, color = Color.LightGray)
                        }
                        Icon(Icons.Default.ChevronRight, null)
                    }
                }
            }
        }
    }
}
