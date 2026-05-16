package com.example.kreedaankana.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Insert
    suspend fun insertTeam(team: TeamEntity)

    @Query("SELECT * FROM teams")
    fun getAllTeams(): Flow<List<TeamEntity>>
}