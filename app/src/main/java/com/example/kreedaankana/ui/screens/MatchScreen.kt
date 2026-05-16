package com.example.kreedaankana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kreedaankana.ui.MatchViewModel
import com.example.kreedaankana.data.AppDatabase

@Composable
fun MatchScreen() {

    val context = androidx.compose.ui.platform.LocalContext.current

    val viewModel: MatchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val matches by viewModel.matches.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Match History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (matches.isEmpty()) {
            Text("No matches found")
        } else {

            LazyColumn {
                items(matches) { match ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            Text("Team A: ${match.teamA}")
                            Text("Team B: ${match.teamB}")
                            Text("Score: ${match.scoreA} - ${match.scoreB}")
                            Text("Date: ${match.date}")
                        }
                    }
                }
            }
        }
    }
}