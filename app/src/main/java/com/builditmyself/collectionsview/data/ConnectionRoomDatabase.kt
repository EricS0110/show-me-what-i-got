package com.builditmyself.collectionsview.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Connection::class], version = 1, exportSchema = false)
abstract class ConnectionRoomDatabase: RoomDatabase() {
    abstract fun connectionDao(): ConnectionDao

    companion object {
        @Volatile
        private var INSTANCE: ConnectionRoomDatabase? = null
        fun getDatabase(context: Context): ConnectionRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConnectionRoomDatabase::class.java,
                    "connection_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}