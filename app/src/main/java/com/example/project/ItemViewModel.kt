package com.example.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project.network.PropertyDC
import java.lang.Exception

class ItemViewModel(private val itemRepository: ItemRepository): ViewModel() {
    var data:MutableLiveData<List<PropertyDC>> = MutableLiveData<List<PropertyDC>>()

    fun getItemsList(){
        try {
                itemRepository.fetchRealestateData()
                data = itemRepository.getRealEstateLiveData()
        }catch (e: Exception){
                Log.d("fetch","In exception ${e.message}")
        }
    }
}

class ItemViewModelFactory(private val itemRepository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(itemRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}