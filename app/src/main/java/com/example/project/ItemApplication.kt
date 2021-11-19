package com.example.project

import android.app.Application
import com.example.project.database.AppDatabase
import com.example.project.utils.SharedPref

class ItemApplication:Application() {
    val database by lazy {AppDatabase.getDatabase(this)}
    val repository by lazy { ItemRepository(database.propertyDao()) }

    override fun onCreate() {
        super.onCreate()
        SharedPref.setupSharedPref(this)
    }
}