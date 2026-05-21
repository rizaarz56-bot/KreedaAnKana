package com.example.kreedaankana.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kreedaankana.ui.BookingViewModel

@Composable
fun BookSlotScreen(viewModel: BookingViewModel = viewModel()) {

    val context = LocalContext.current
    val bookings = viewModel.bookings

    val slots = listOf(
        "4-5 PM", "5-6 PM", "6-7 PM", "7-8 PM"
    )

    var teamName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Book a Slot", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = teamName,
            onValueChange = { teamName = it },
            label = { Text("Team Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Time Slot")

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                items(slots) { slot ->

                    val isBooked = bookings.any { it.time == slot }

                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {

                                if (teamName.isEmpty()) {
                                    Toast.makeText(context, "Enter team name", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }

                                viewModel.bookSlot(teamName, slot, "Village Ground") { success ->
                                    if (success) {
                                        Toast.makeText(context, "Booked Successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Slot Already Booked", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isBooked)
                                MaterialTheme.colorScheme.errorContainer
                            else
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(slot)
                            if (isBooked) {
                                Text("Booked", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        )
    }
}