package com.example.kreedaankana.data

import android.content.Context
import androidx.room.Room

object DatabaseInstance {

    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {

        if (database == null) {

            database = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "kreeda_database"
            ).build()
        }

        return database!!
    }
}