package com.example.kreedaankana.ui

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kreedaankana.data.BookingEntity
import com.example.kreedaankana.data.DatabaseInstance
import com.example.kreedaankana.repository.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel(application: Application) :
    AndroidViewModel(application) {

    private val dao =
        DatabaseInstance.getDatabase(application).bookingDao()

    private val repository =
        BookingRepository(dao)

    val bookings =
        mutableStateListOf<BookingEntity>()

    init {
        loadBookings()
    }

    fun loadBookings() {

        viewModelScope.launch {

            bookings.clear()

            bookings.addAll(
                repository.getAllBookings()
            )
        }
    }

    fun bookSlot(
        team: String,
        time: String,
        onResult: (Boolean) -> Unit
    ) {

        viewModelScope.launch {

            val success =
                repository.bookSlot(team, time)

            loadBookings()

            onResult(success)
        }
    }
}