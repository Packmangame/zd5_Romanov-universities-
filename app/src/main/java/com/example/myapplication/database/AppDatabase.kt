package com.example.myapplication.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.myapplication.DAO.UniversDao
import com.example.myapplication.DAO.UserDao
import com.example.myapplication.model.University
import com.example.myapplication.model.Users

@Database(entities = [Users::class,University::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun universDao(): UniversDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Users"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}