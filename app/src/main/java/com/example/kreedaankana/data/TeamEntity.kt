package com.example.kreedaankana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val teamName: String,

    val captainName: String = ""
)

