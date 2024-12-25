package com.example.myapplication.model

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class Users(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val email: String,
    val name:String,
    val login: String,
    val password: String)
