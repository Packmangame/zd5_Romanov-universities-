package com.example.myapplication.DAO
import androidx.room.*
import com.example.myapplication.model.University

@Dao
interface UniversDao {
    @Query("SELECT*FROM university_table WHERE UniverName=:name ")
    suspend fun getCharacterByName(name: String): University?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: University)

    @Update
    suspend fun updateData(data: University)

    @Delete
    suspend fun deleteData(data: University)

    @Query("SELECT * FROM university_table")
    suspend fun getAllData(): List<University>
}