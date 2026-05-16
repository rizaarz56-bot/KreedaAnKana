package com.example.kreedaankana.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.kreedaankana.Booking
import com.example.kreedaankana.HomeScreen
import com.example.kreedaankana.BookSlotScreen
import com.example.kreedaankana.ChallengeScreen
import com.example.kreedaankana.ScoreScreen
import com.example.kreedaankana.AIScreen
import com.example.kreedaankana.ProfileScreen
import com.example.kreedaankana.MyBookingsScreen
import com.example.kreedaankana.ui.screens.MatchScreen
import com.example.kreedaankana.ui.screens.TeamScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    val bookings = remember { mutableStateListOf<Booking>() }

    LaunchedEffect(Unit) {

        val db = FirebaseFirestore.getInstance()

        db.collection("bookings")
            .addSnapshotListener { snapshot, _ ->

                if (snapshot == null) return@addSnapshotListener

                bookings.clear()

                for (doc in snapshot.documents) {

                    val team = doc.getString("team") ?: ""
                    val time = doc.getString("time") ?: ""

                    bookings.add(Booking(team, time))
                }
            }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(navController, bookings)
        }

        composable("book") {
            BookSlotScreen(bookings, navController)
        }

        composable("challenge") {
            ChallengeScreen()
        }

        composable("score") {
            ScoreScreen()
        }

        composable("mybookings") {
            MyBookingsScreen(bookings)
        }

        composable("profile") {
            ProfileScreen()
        }
        composable("match") {
            MatchScreen()
        }
        composable("teams") {
            TeamScreen()
        }
        composable("ai") {
            AIScreen()
        }
    }
}