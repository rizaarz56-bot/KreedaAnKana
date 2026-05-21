package com.example.kreedaankana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaankana.data.AppDatabase
import com.example.kreedaankana.data.TeamEntity

@Composable
fun TeamScreen() {

    val context = LocalContext.current
    var teams by remember { mutableStateOf(listOf<TeamEntity>()) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        db.teamDao().getAllTeams().collect { list ->
            teams = list
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp)
    ) {

        Text(
            text = "Teams",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001B5E)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (teams.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No teams found", color = Color.Gray)
            }
        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(teams) { team ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {

                        Column(modifier = Modifier.padding(20.dp)) {

                            // TEAM NAME WITH BADGE
                            Surface(
                                color = Color(0xFFE3F2FD),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = team.teamName.uppercase(),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1565C0)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // CAPTAIN SECTION
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = "Captain: ",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                
                                Text(
                                    text = team.captainName.ifEmpty { "Not Assigned" },
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
