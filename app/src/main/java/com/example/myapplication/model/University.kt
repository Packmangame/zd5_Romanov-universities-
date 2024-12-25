package com.example.myapplication.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "university_table")
data class University(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val UniverName: String,
    val UniverWeb:String,
    val UniverCountry: String)