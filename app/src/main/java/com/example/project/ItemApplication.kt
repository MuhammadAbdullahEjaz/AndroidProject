package com.example.project

import android.app.Application
import com.example.project.database.AppDatabase

class ItemApplication:Application() {
    val database by lazy {AppDatabase.getDatabase(this)}
    val sharedPreference by lazy { this.getSharedPreferences(MainActivity.Main.SharedPreferenceFile, MODE_PRIVATE) }
    val repository by lazy { ItemRepository(database.propertyDao(),sharedPreference) }
}