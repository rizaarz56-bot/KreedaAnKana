package com.example.kreedaankana.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookingEntity::class,
        MatchEntity::class,
        TeamEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookingDao(): BookingDao
    abstract fun matchDao(): MatchDao
    abstract fun teamDao(): TeamDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kreeda_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}