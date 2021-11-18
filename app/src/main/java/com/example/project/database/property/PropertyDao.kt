package com.example.project.database.property

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PropertyDao {
    @Insert
    suspend fun insertProperty(property: Property)

    @Query("SELECT * FROM property")
    fun fetchallProperty(): LiveData<List<Property>>

    @Update
    suspend fun updateProperty(property: Property)

    @Query("SELECT id FROM Property WHERE id= :id")
    suspend fun isIdPresent(id:Int):Int
}