package com.example.project

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project.database.property.Property
import com.example.project.database.property.PropertyDao
import com.example.project.network.ItemApi
import com.example.project.network.PropertyDC
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
    private val sharedPreferences: SharedPreferences
) {
    private var ItemApiService: ItemApi? = null
    private var realestateData: MutableLiveData<List<PropertyDC>> = MutableLiveData()

    init {
        ItemApiService = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemApi::class.java)
    }

    fun fetchRealestateData() {
        if (sharedPreferences.contains(MainActivity.Main.FIRST)) {
            val data1: MutableList<PropertyDC> = mutableListOf()
            var notFirst: Boolean = sharedPreferences.getBoolean(MainActivity.Main.FIRST, false)
            if (notFirst) {
                getallProperty().observeForever { list ->
                    list.forEach { p ->
                        data1.add(
                            PropertyDC(
                                id = p.id.toString(),
                                price = p.price,
                                type = p.type,
                                img_src = p.img_src
                            )
                        )
                    }
                    Log.d("resp","data fetched from DB")
                    realestateData.postValue(data1)
                }
            }
        } else {
            ItemApiService?.getRealEstate()?.enqueue(object : retrofit2.Callback<List<PropertyDC>> {
                override fun onResponse(
                    call: Call<List<PropertyDC>>,
                    response: Response<List<PropertyDC>>
                ) {
                    if (response.code() == 200) {
                        Log.d("resp","data fetched from API")
                        realestateData.postValue(response.body())
                        CoroutineScope(Dispatchers.IO).launch {
                            response.body()?.forEach { p ->
                                val id: Int? = isIdPresent(p.id.toInt())
                                if (id != null) {
                                    updatePropertyR(p)
                                } else {
                                    insertPropertyR(p)
                                }
                            }
                        }
                    }
                }


                override fun onFailure(call: Call<List<PropertyDC>>, t: Throwable) {
                    val data: MutableList<PropertyDC> = mutableListOf()
                    getallProperty().observeForever { list ->
                        list.forEach { p ->
                            data.add(
                                PropertyDC(
                                    id = p.id.toString(),
                                    price = p.price,
                                    type = p.type,
                                    img_src = p.img_src
                                )
                            )
                        }
                        realestateData.postValue(data.toList())
                    }
                }
            })
        }
    }

    fun getRealEstateLiveData(): MutableLiveData<List<PropertyDC>> {
        return realestateData
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

    suspend fun isIdPresent(id: Int): Int {
        return propertyDao.isIdPresent(id)
    }

    fun getallProperty(): LiveData<List<Property>> {
        return propertyDao.fetchallProperty()
    }
}
