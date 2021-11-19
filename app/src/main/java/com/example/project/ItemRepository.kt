package com.example.project

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project.database.property.Property
import com.example.project.database.property.PropertyDao
import com.example.project.network.ItemApi
import com.example.project.network.PropertyDC
import com.example.project.utils.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemRepository(
    private val propertyDao: PropertyDao,
) {
    private var ItemApiService: ItemApi? = null
    private var realestateData: MutableLiveData<List<Property>>  = MutableLiveData()
    init {
        ItemApiService = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemApi::class.java)
    }

    fun fetchRealestateData():LiveData<List<Property>> {
        if (!SharedPref.getPrefNotFirst()) {
            ItemApiService?.getRealEstate()?.enqueue(object : retrofit2.Callback<List<PropertyDC>> {
                override fun onResponse(
                    call: Call<List<PropertyDC>>,
                    response: Response<List<PropertyDC>>
                ) {
                    if (response.code() == 200) {
                        Log.d("resp", "data fetched from API")
                        CoroutineScope(Dispatchers.IO).launch {
                            insertPropertyAllR(response.body())
                        }
                    }
                }

                override fun onFailure(call: Call<List<PropertyDC>>, t: Throwable) {
                    Log.d("resp","on failure")
                    SharedPref.setPrefNotFirst(false)
                }
            })
            return getallPropertyR()
        } else {
            return getallPropertyR()
        }
    }

    suspend fun insertPropertyR(p: PropertyDC) {
        propertyDao.insertProperty(Property(p.id.toInt(), p.price, p.type, p.img_src))
    }

    suspend fun insertPropertyAllR(property: List<PropertyDC>?) {
        if (property != null) {
            property.forEach { p ->
                propertyDao.insertProperty(Property(p.id.toInt(), p.price, p.type, p.img_src))
            }
        }
    }

    suspend fun updatePropertyAllR(property: List<PropertyDC>?) {
        if (property != null) {
            property.forEach { p ->
                propertyDao.updateProperty(Property(p.id.toInt(), p.price, p.type, p.img_src))
            }
        }
    }

    suspend fun updatePropertyR(p: PropertyDC) {
        propertyDao.updateProperty(Property(p.id.toInt(), p.price, p.type, p.img_src))
    }

    suspend fun isIdPresentR(id: Int): Int {
        return propertyDao.isIdPresent(id)
    }

    private fun getallPropertyR(): LiveData<List<Property>> {
        Log.d("resp","from DB")
        return propertyDao.fetchallProperty()
    }
}
