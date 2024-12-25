package com.example.myapplication.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.Users

@Dao
interface UserDao {
    @Insert
    suspend fun insert(users: Users)

    @Query("SELECT * FROM users_table WHERE name = :username LIMIT 1")
    public suspend fun getUserByUsername(username: String): Users?



}