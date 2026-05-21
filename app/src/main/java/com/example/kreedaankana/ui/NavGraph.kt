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
import com.example.kreedaankana.ProfileScreen
import com.example.kreedaankana.MyBookingsScreen
import com.example.kreedaankana.ui.screens.MatchScreen
import com.example.kreedaankana.ui.screens.TeamScreen

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kreedaankana.ui.screens.LoginScreen
import com.example.kreedaankana.ui.screens.SignupScreen
import com.example.kreedaankana.ui.screens.SplashScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

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
                    val ground = doc.getString("ground") ?: "Village Ground"

                    bookings.add(Booking(doc.id, team, time, ground))
                }
            }
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(navController, authViewModel)
        }

        composable("login") {
            LoginScreen(navController, authViewModel)
        }

        composable("signup") {
            SignupScreen(navController, authViewModel)
        }

        composable("home") {
            HomeScreen(navController, bookings)
        }

        composable("book") {
            BookSlotScreen(bookings, navController)
        }

        composable("challenge") {
            ChallengeScreen(navController)
        }

        composable("score") {
            ScoreScreen()
        }

        composable("mybookings") {
            MyBookingsScreen(bookings)
        }

        composable("profile") {
            ProfileScreen(navController, authViewModel)
        }
        composable("match") {
            MatchScreen()
        }
        composable("teams") {
            TeamScreen()
        }
    }
}