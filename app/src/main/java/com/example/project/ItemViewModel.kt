package com.example.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project.database.property.Property
import com.example.project.network.PropertyDC
import java.lang.Exception

class ItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    fun getItemsList():LiveData<List<Property>> {
        return itemRepository.fetchRealestateData()
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