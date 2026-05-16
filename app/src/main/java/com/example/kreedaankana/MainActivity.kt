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
data class Booking(
    val team: String,
    val time: String
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

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
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
                    title = "AI NAME",
                    icon = Icons.Default.SmartToy,
                    iconColor = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("ai")
                }

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
                QuickAccessCard(
                    title = "PROFILE",
                    icon = Icons.Default.Person,
                    iconColor = Color(0xFF26C6DA),
                    modifier = Modifier.weight(1f)
                ) {navController.navigate("profile")}
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
                                text = "Village Ground ",
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
@Composable
fun BookSlotScreen(
    bookings: SnapshotStateList<Booking>,
    navController: NavHostController
){

    val teamName = remember { mutableStateOf("") }
    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

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
            modifier = Modifier.fillMaxWidth()
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
                    it.time == newTime
                }

                if (isConflict) {
                    Toast.makeText(context, "Slot already booked!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

                val booking = hashMapOf(
                    "team" to teamName.value,
                    "time" to newTime
                )
                val bookingEntity = BookingEntity(
                    team = teamName.value,
                    time = newTime
                )

                scope.launch {
                    val dbRoom = AppDatabase.getDatabase(context)
                    dbRoom.bookingDao().insertBooking(bookingEntity)
                }

                db.collection("bookings")
                    .add(booking)
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


@Composable
fun ChallengeScreen() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val challengeText = remember { mutableStateOf("") }
    val replyText = remember { mutableStateOf("") }

    val challenges = remember { mutableStateListOf<Pair<String, String>>() }

    // 🔥 Load challenges
    LaunchedEffect(Unit) {
        db.collection("challenges")
            .addSnapshotListener { snapshot, _ ->

                challenges.clear()

                snapshot?.documents?.forEach { doc ->
                    val text = doc.getString("text") ?: ""
                    val reply = doc.getString("reply") ?: ""
                    challenges.add(text to reply)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Challenge Board",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // INPUT
        OutlinedTextField(
            value = challengeText.value,
            onValueChange = { challengeText.value = it },
            label = { Text("Enter Challenge") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                if (challengeText.value.isEmpty()) {
                    Toast.makeText(context, "Enter challenge", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val data = hashMapOf(
                    "text" to challengeText.value,
                    "reply" to ""
                )

                db.collection("challenges")
                    .add(data)

                challengeText.value = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Post Challenge")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // LIST
        challenges.forEachIndexed { index, item ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {

                Column(modifier = Modifier.padding(12.dp)) {

                    Text(
                        text = item.first,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Reply: ${item.second}")

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = replyText.value,
                        onValueChange = { replyText.value = it },
                        label = { Text("Write reply") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {

                            val docRef = db.collection("challenges").document()

                            db.collection("challenges")
                                .whereEqualTo("text", item.first)
                                .get()
                                .addOnSuccessListener { docs ->

                                    for (doc in docs) {
                                        db.collection("challenges")
                                            .document(doc.id)
                                            .update("reply", replyText.value)
                                    }
                                }

                        }
                    ) {
                        Text("Reply")
                    }
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
        mutableStateListOf<Triple<String, String, String>>()
    }

    LaunchedEffect(Unit) {

        db.collection("scores")
            .addSnapshotListener { snapshot, _ ->

                scores.clear()

                snapshot?.documents?.forEach { doc ->

                    val t1 = doc.getString("team1") ?: ""
                    val t2 = doc.getString("team2") ?: ""
                    val res = doc.getString("result") ?: ""

                    scores.add(Triple(t1, t2, res))
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Score Wall",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = team1.value,
            onValueChange = { team1.value = it },
            label = { Text("Team 1 Score") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = team2.value,
            onValueChange = { team2.value = it },
            label = { Text("Team 2 Score") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = result.value,
            onValueChange = { result.value = it },
            label = { Text("Match Result") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val data = hashMapOf(
                    "team1" to team1.value,
                    "team2" to team2.value,
                    "result" to result.value
                )

                db.collection("scores")
                    .add(data)
                val match = MatchEntity(
                    teamA = team1.value,
                    teamB = team2.value,
                    scoreA = team1.value.toIntOrNull() ?: 0,
                    scoreB = team2.value.toIntOrNull() ?: 0,
                    date = java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date())
                )

                scope.launch {
                    val dbRoom = AppDatabase.getDatabase(context)
                    dbRoom.matchDao().insertMatch(match)
                }

                team1.value = ""
                team2.value = ""
                result.value = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Post Score")
        }

        Spacer(modifier = Modifier.height(20.dp))

        scores.forEach { score ->

            scores.forEach { score ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "🏆 Match Result",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = score.first,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = score.second,
                            fontSize = 22.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFC8E6C9),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {

                            Text(
                                text = score.third,
                                color = Color(0xFF1B5E20),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }}
@Composable
fun MyBookingsScreen(
    bookings: SnapshotStateList<Booking>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "My Bookings",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (bookings.isEmpty()) {

            Text(
                text = "No bookings available",
                fontSize = 18.sp,
                color = Color.Gray
            )

        } else {

            bookings.forEach { booking ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Village Ground",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = booking.time,
                            color = Color(0xFF1565C0),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Team: ${booking.team}",
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ProfileScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color(0xFF1565C0),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Riza",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Village Sports Organizer",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Favorite Sport: Cricket",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Ground: Village Ground",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "App: Kreeda Ankana",
                    fontSize = 18.sp
                )
            }
        }
    }
}
@Composable
fun AIScreen() {

    var teamName = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "AI Team Generator",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF42A5F5)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {

                val names = listOf(
                    "Warriors",
                    "Thunder Kings",
                    "Falcons",
                    "Blaze Riders",
                    "Power Strikers"
                )

                teamName.value = names.random()
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),

            shape = RoundedCornerShape(16.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF42A5F5)
            )
        ) {

            Text(
                text = "Generate Team Name",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {

            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI",
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (teamName.value.isEmpty())
                        "Click button to generate name"
                    else
                        teamName.value,

                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

