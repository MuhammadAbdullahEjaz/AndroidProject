package com.example.project.database.property

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PropertyDao {
    @Insert
    suspend fun insertProperty(property:Property)

    @Query("SELECT * FROM property")
    fun fetchallProperty(): LiveData<List<Property>>
}