package com.example.kreedaankana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.vector.ImageVector
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.LaunchedEffect
import com.example.kreedaankana.ui.NavGraph
import com.example.kreedaankana.data.BookingEntity
import com.example.kreedaankana.data.AppDatabase
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.kreedaankana.data.TeamEntity
import com.example.kreedaankana.data.MatchEntity
import com.example.kreedaankana.ui.AuthViewModel
import com.google.firebase.auth.FirebaseUser
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign

data class Booking(
    val id: String = "",
    val team: String,
    val time: String,
    val ground: String = "Village Ground"
)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavGraph()
        }
    }}
@Composable
fun HomeScreen(
    navController: NavHostController,
    bookings: SnapshotStateList<Booking>
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F5F5))
    ) {

        // TOP BAR

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001B5E))
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )

            Text(
                text = "Kreeda Ankana",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.navigate("profile") }
                )
            }
        }

        // BANNER IMAGE

        Image(
            painter = painterResource(id = R.drawable.sports_banner),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // QUICK ACCESS

        Text(
            text = "Quick Access",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {

            // ROW 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                QuickAccessCard(
                    title = "BOOK SLOT",
                    icon = Icons.Default.DateRange,
                    iconColor = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("book")
                }

                QuickAccessCard(
                    title = "CHALLENGE",
                    icon = Icons.Default.Groups,
                    iconColor = Color(0xFFFF5722),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("challenge")
                }

                QuickAccessCard(
                    title = "SCORE",
                    icon = Icons.Default.EmojiEvents,
                    iconColor = Color(0xFF7E57C2),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("score")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ROW 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                QuickAccessCard(
                    title = "MY BOOKINGS",
                    icon = Icons.Default.DateRange,
                    iconColor = Color(0xFFFFB300),
                    modifier = Modifier.weight(1f)
                ) {navController.navigate("mybookings")}

                QuickAccessCard(
                    title = "MATCHES",
                    icon = Icons.Default.SportsCricket,
                    iconColor = Color(0xFF009688),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("match")
                }
                QuickAccessCard(
                    title = "TEAMS",
                    icon = Icons.Default.Groups,
                    iconColor = Color(0xFF673AB7),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("teams")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // UPCOMING BOOKINGS TITLE

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Ground Calendar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "View All",
                color = Color(0xFFFF6F00),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BOOKING CARD

        if (bookings.isNotEmpty()) {

            bookings.forEach { booking ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(modifier = Modifier.padding(20.dp)) {

                        // CITY STADIUM + ICON
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar",
                                tint = Color(0xFF1565C0)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = booking.ground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = booking.time,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Team: ${booking.team}",
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )

                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(0xFFC8E6C9),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Confirmed",
                                    color = Color(0xFF1B5E20),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

        } else {

            Text(
                text = "No bookings yet",
                modifier = Modifier.padding(start = 16.dp),
                color = Color.Gray,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
@Composable
fun QuickAccessCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(105.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(0xFFE0E0E0)
        ),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSlotScreen(
    bookings: SnapshotStateList<Booking>,
    navController: NavHostController
){

    val teamName = remember { mutableStateOf("") }
    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val grounds = listOf(
        "Gram Panchayat Maidan",
        "Nisarga Layout Arena",
        "Govt Model School Ground"
    )
    val selectedGround = remember { mutableStateOf(grounds[0]) }
    val expanded = remember { mutableStateOf(false) }

    val timeSlots = listOf(
        "4:00 - 5:00 PM",
        "5:00 - 6:00 PM",
        "6:00 - 7:00 PM",
        "7:00 - 8:00 PM"
    )
    val selectedSlot = remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Book Your Slot",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(30.dp))



        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = teamName.value,
            onValueChange = {
                teamName.value = it
            },
            label = {
                Text("Team Name")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedGround.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Ground") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                grounds.forEach { ground ->
                    DropdownMenuItem(
                        text = { Text(ground) },
                        onClick = {
                            selectedGround.value = ground
                            expanded.value = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Select Date",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val calendar = java.util.Calendar.getInstance()

                val datePicker = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->

                        calendar.set(year, month, dayOfMonth)

                        val formatter = java.text.SimpleDateFormat("d MMMM yyyy", java.util.Locale.getDefault())

                        selectedDate.value = formatter.format(calendar.time)
                    },
                    calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH),
                    calendar.get(java.util.Calendar.DAY_OF_MONTH)
                )

                datePicker.show()


            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0)
            )
        ) {
            Text(
                if (selectedDate.value.isEmpty())
                    "Pick Date"
                else
                    selectedDate.value
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Select Time Slot",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                timeSlots.take(2).forEach { slot ->

                    val isSelected = selectedSlot.value == slot

                    Card(
                        onClick = {
                            selectedSlot.value = slot
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (isSelected)
                                    Color(0xFF1565C0)
                                else
                                    Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFF1565C0)
                        )
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = slot,
                                color =
                                    if (isSelected)
                                        Color.White
                                    else
                                        Color(0xFF1565C0),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                timeSlots.drop(2).forEach { slot ->

                    val isSelected = selectedSlot.value == slot

                    Card(
                        onClick = {
                            selectedSlot.value = slot
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (isSelected)
                                    Color(0xFF1565C0)
                                else
                                    Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFF1565C0)
                        )
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = slot,
                                color =
                                    if (isSelected)
                                        Color.White
                                    else
                                        Color(0xFF1565C0),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (teamName.value.isEmpty() ||
                    selectedSlot.value.isEmpty() ||
                    selectedDate.value.isEmpty()
                ) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val newTime = "${selectedDate.value} | ${selectedSlot.value}"

                val isConflict = bookings.any {
                    it.time == newTime && it.ground == selectedGround.value
                }

                if (isConflict) {
                    Toast.makeText(context, "Slot already booked for this ground!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

                val bookingMap = hashMapOf(
                    "team" to teamName.value,
                    "time" to newTime,
                    "ground" to selectedGround.value
                )
                val bookingEntity = BookingEntity(
                    team = teamName.value,
                    time = newTime,
                    ground = selectedGround.value
                )

                scope.launch {
                    val dbRoom = AppDatabase.getDatabase(context)
                    dbRoom.bookingDao().insertBooking(bookingEntity)
                }

                db.collection("bookings")
                    .add(bookingMap)
                    .addOnSuccessListener { val team = TeamEntity(
                        teamName = teamName.value,
                        captainName = ""
                    )

                        scope.launch {
                            val dbRoom = AppDatabase.getDatabase(context)
                            dbRoom.teamDao().insertTeam(team)
                        }


                        Toast.makeText(context, "Booking Successful", Toast.LENGTH_SHORT).show()

                        navController.navigate("home")
                    }
                    .addOnFailureListener {

                        Toast.makeText(context, "Failed to book slot", Toast.LENGTH_SHORT).show()
                    }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),

            shape = RoundedCornerShape(16.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0)
            )
        ) {
            Text(
                text = "Confirm Booking",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(navController: NavHostController) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val challengeText = remember { mutableStateOf("") }
    val challenges = remember { mutableStateListOf<Map<String, Any>>() }

    // 🔥 Load challenges
    LaunchedEffect(Unit) {
        db.collection("challenges")
            .addSnapshotListener { snapshot, _ ->
                challenges.clear()
                snapshot?.documents?.forEach { doc ->
                    val data = doc.data ?: mutableMapOf()
                    data["id"] = doc.id
                    challenges.add(data)
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Challenge Board", 
                        color = Color.White, 
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, 
                            contentDescription = "Back", 
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF001B5E)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            // INPUT AREA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = challengeText.value,
                        onValueChange = { challengeText.value = it },
                        placeholder = { Text("Enter your challenge (e.g. Team A vs Team B)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1565C0),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (challengeText.value.isEmpty()) {
                                Toast.makeText(context, "Please enter a challenge", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val data = hashMapOf(
                                "text" to challengeText.value,
                                "date" to SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                                "time" to SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
                                "sport" to "Cricket",
                                "status" to "Open"
                            )

                            db.collection("challenges").add(data)
                            challengeText.value = ""
                            Toast.makeText(context, "Challenge Posted!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Text(
                            text = "POST CHALLENGE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Text(
                text = "Recent Challenges",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color(0xFF001B5E)
            )

            // LIST SECTION
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(challenges) { challenge ->
                    ChallengeListItem(challenge) { id ->
                        db.collection("challenges").document(id).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Challenge Deleted", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeListItem(data: Map<String, Any>, onDelete: (String) -> Unit) {
    val id = data["id"] as? String ?: ""
    val text = data["text"] as? String ?: ""
    val teams = text.split(" vs ", ignoreCase = true)
    val team1 = teams.getOrNull(0)?.trim() ?: "Team A"
    val team2 = teams.getOrNull(1)?.trim() ?: "Team B"
    val sport = data["sport"] as? String ?: "Cricket"
    val time = data["time"] as? String ?: "00:00"
    val date = data["date"] as? String ?: "Date"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // VS SECTION
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = team1,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFF1565C0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VS", 
                        color = Color.White, 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = team2,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                
                IconButton(onClick = { onDelete(id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFF1565C0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(12.dp))

            // BOTTOM DETAILS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = sport, 
                        color = Color.Gray, 
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$time | $date",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF001B5E)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = "Open",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreScreen() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    val team1 = remember { mutableStateOf("") }
    val team2 = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }

    val scores = remember {
        mutableStateListOf<Map<String, Any>>()
    }

    LaunchedEffect(Unit) {

        db.collection("scores")
            .addSnapshotListener { snapshot, _ ->

                scores.clear()

                snapshot?.documents?.forEach { doc ->
                    val data = doc.data ?: mutableMapOf()
                    data["id"] = doc.id
                    scores.add(data)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Score Wall",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001B5E)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = team1.value,
                    onValueChange = { team1.value = it },
                    label = { Text("Team 1 Score") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = team2.value,
                    onValueChange = { team2.value = it },
                    label = { Text("Team 2 Score") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = result.value,
                    onValueChange = { result.value = it },
                    label = { Text("Match Result") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (team1.value.isEmpty() || team2.value.isEmpty() || result.value.isEmpty()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val data = hashMapOf(
                            "team1" to team1.value,
                            "team2" to team2.value,
                            "result" to result.value,
                            "date" to java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(java.util.Date())
                        )

                        db.collection("scores").add(data)
                        
                        val match = MatchEntity(
                            teamA = team1.value,
                            teamB = team2.value,
                            scoreA = team1.value.split("/").firstOrNull()?.toIntOrNull() ?: 0,
                            scoreB = team2.value.split("/").firstOrNull()?.toIntOrNull() ?: 0,
                            date = data["date"] as String
                        )

                        scope.launch {
                            val dbRoom = AppDatabase.getDatabase(context)
                            dbRoom.matchDao().insertMatch(match)
                        }

                        team1.value = ""
                        team2.value = ""
                        result.value = ""
                        Toast.makeText(context, "Score Posted!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Text("Post Score", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recent Results",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001B5E)
        )

        Spacer(modifier = Modifier.height(12.dp))

        scores.reversed().forEach { score ->
            val id = score["id"] as? String ?: ""
            val t1 = score["team1"] as? String ?: ""
            val t2 = score["team2"] as? String ?: ""
            val res = score["result"] as? String ?: ""
            val dateStr = score["date"] as? String ?: ""

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Match Result",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            Text(
                                text = t1,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                            Text(text = "Overs: 20.0", fontSize = 12.sp, color = Color.Gray)
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF1565C0).copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "VS",
                                color = Color(0xFF1565C0),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            Text(
                                text = t2,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                            Text(text = "Overs: 20.0", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            db.collection("scores").document(id).delete()
                                .addOnSuccessListener {
                                    scope.launch {
                                        val dbRoom = AppDatabase.getDatabase(context)
                                        dbRoom.matchDao().deleteMatch(t1, t2, dateStr)
                                    }
                                    Toast.makeText(context, "Result Deleted", Toast.LENGTH_SHORT).show()
                                }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFF1565C0)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = res,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MyBookingsScreen(
    bookings: SnapshotStateList<Booking>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "My Bookings",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001B5E)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (bookings.isEmpty()) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No bookings available",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookings) { booking ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0F7FF) // Soft light blue tint
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE3F2FD))
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = booking.ground,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF001B5E),
                                    modifier = Modifier.weight(1f)
                                )

                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFFFEBEE), // Light red background
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    IconButton(onClick = {
                                        db.collection("bookings").document(booking.id).delete()
                                            .addOnSuccessListener {
                                                scope.launch {
                                                    val dbRoom = AppDatabase.getDatabase(context)
                                                    dbRoom.bookingDao().deleteBookingByTimeAndTeam(booking.team, booking.time)
                                                }
                                                Toast.makeText(context, "Booking Deleted", Toast.LENGTH_SHORT).show()
                                            }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(0xFFD32F2F),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    tint = Color(0xFF1565C0),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = booking.time,
                                    color = Color.DarkGray,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Team: ${booking.team}",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val userEmail = authViewModel.currentUser.value?.email ?: "guest@kreeda.com"
    val userName = "Riza" // Prominent Name as requested

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // USER IDENTITY HEADER
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(4.dp, Color(0xFF001B5E)),
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    tint = Color(0xFF001B5E),
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = userName,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF001B5E)
        )

        Text(
            text = userEmail,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = Color(0xFFE3F2FD),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Village Sports Organizer",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // PREMIUM INFORMATION SHOWCASE CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                ProfileDetailRow(
                    icon = Icons.Default.SportsCricket,
                    label = "Favorite Sport",
                    value = "Cricket",
                    iconColor = Color(0xFFFF6F00)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color(0xFFF0F0F0))

                ProfileDetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Assigned Ground",
                    value = "Village Ground",
                    iconColor = Color(0xFF4CAF50)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color(0xFFF0F0F0))

                ProfileDetailRow(
                    icon = Icons.Default.Info,
                    label = "App Title",
                    value = "Kreeda Ankana",
                    iconColor = Color(0xFF2196F3)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // REFINED LOGOUT BUTTON
        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo(0)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Logout",
                    color = Color(0xFFD32F2F),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProfileDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(10.dp),
            color = iconColor.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

