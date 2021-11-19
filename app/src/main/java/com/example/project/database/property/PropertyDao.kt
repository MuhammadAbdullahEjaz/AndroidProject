package com.example.project.database.property

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Query("SELECT * FROM property")
    fun fetchallProperty(): LiveData<List<Property>>

    @Update
    suspend fun updateProperty(property: Property)

    @Query("SELECT id FROM Property WHERE id= :id")
    suspend fun isIdPresent(id:Int):Int
}