package com.example.project.database.property

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "price") val price: Int,
    @NonNull @ColumnInfo(name = "type") val type: String,
    @NonNull @ColumnInfo(name = "img_src") val img_src: String
)