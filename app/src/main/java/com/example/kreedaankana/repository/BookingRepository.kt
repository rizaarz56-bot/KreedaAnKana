package com.example.kreedaankana.repository

import com.example.kreedaankana.data.BookingDao
import com.example.kreedaankana.data.BookingEntity

class BookingRepository(
    private val dao: BookingDao
) {

    suspend fun getAllBookings(): List<BookingEntity> {
        return dao.getAllBookings()
    }

    suspend fun bookSlot(
        team: String,
        time: String,
        ground: String
    ): Boolean {

        val existing = dao.getBookingByTime(time)

        return if (existing == null) {

            dao.insertBooking(
                BookingEntity(
                    team = team,
                    time = time,
                    ground = ground
                )
            )

            true

        } else {

            false
        }
    }
}