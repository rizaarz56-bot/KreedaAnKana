package com.example.kreedaankana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.kreedaankana.data.AppDatabase
import com.example.kreedaankana.data.TeamEntity
import androidx.compose.runtime.collectAsState

@Composable
fun TeamScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var teams by remember { mutableStateOf(listOf<TeamEntity>()) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)

        db.teamDao().getAllTeams().collect { list ->
            teams = list
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "Teams",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (teams.isEmpty()) {
            Text("No teams found")
        } else {

            LazyColumn {
                items(teams) { team ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Column(modifier = Modifier.padding(12.dp)) {

                            Text("Team Name: ${team.teamName}")
                            Text("Captain: ${team.captainName}")
                        }
                    }
                }
            }
        }
    }
}