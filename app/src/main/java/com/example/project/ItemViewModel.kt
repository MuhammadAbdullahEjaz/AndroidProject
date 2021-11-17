package com.example.project

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.project.database.AppDatabase
import com.example.project.network.PropertyDC
import java.lang.Exception

class ItemViewModel(application: Application): AndroidViewModel(application) {
    var data:MutableLiveData<List<PropertyDC>> = MutableLiveData<List<PropertyDC>>()
    private var itemRespository: ItemRepository
    init {
        val propertyDao = AppDatabase.getDatabase(application).propertyDao()
        itemRespository = ItemRepository(propertyDao)
    }
    fun getItemsList(){
        try {
                itemRespository.fetchRealestateData()
                data = itemRespository.getRealEstateLiveData()

        }catch (e: Exception){
                Log.d("fetch","In exception ${e.message}")
        }
    }
}