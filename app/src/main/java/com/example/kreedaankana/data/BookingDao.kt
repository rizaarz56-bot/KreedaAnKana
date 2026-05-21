package com.example.kreedaankana.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings WHERE time = :time LIMIT 1")
    suspend fun getBookingByTime(time: String): BookingEntity?

    @Query("SELECT * FROM bookings")
    suspend fun getAllBookings(): List<BookingEntity>

    @Query("DELETE FROM bookings WHERE time = :time AND team = :team")
    suspend fun deleteBookingByTimeAndTeam(team: String, time: String)
}