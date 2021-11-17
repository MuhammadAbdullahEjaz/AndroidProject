package com.example.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project.database.property.Property
import com.example.project.database.property.PropertyDao
import com.example.project.network.ItemApi
import com.example.project.network.PropertyDC
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemRepository(private val propertyDao:PropertyDao){
    private var ItemApiService: ItemApi? = null
    private var realestateData: MutableLiveData<List<PropertyDC>> = MutableLiveData()
    init {
        ItemApiService = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemApi::class.java)
    }

    fun fetchRealestateData(){
        ItemApiService?.getRealEstate()?.enqueue(object :retrofit2.Callback<List<PropertyDC>>{
            override fun onResponse(
                call: Call<List<PropertyDC>>,
                response: Response<List<PropertyDC>>
            ) {
                if(response.code() == 200){
                    realestateData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<PropertyDC>>, t: Throwable) {
                Log.d("fetch", "error in getRealestateData()")
            }

        })
    }
    fun getRealEstateLiveData():MutableLiveData<List<PropertyDC>>{
        return realestateData
    }

    suspend fun insertPropertyR(property: Property){
        propertyDao.insertProperty(property)
    }
    suspend fun getallProperty():LiveData<List<Property>>{
        return propertyDao.fetchallProperty()
    }
}
