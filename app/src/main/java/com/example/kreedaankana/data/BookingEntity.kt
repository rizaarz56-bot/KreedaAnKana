package com.example.kreedaankana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val team: String,
    val time: String,
    val ground: String = "Village Ground"
)