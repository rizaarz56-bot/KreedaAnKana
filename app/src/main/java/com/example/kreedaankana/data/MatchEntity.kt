package com.example.kreedaankana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val teamA: String,
    val teamB: String,

    val scoreA: Int,
    val scoreB: Int,

    val date: String
)

