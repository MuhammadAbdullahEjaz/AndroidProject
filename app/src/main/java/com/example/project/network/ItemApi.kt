package com.example.project.network

import retrofit2.Call
import retrofit2.http.GET

interface ItemApi {
    @GET("realestate")
    fun getRealEstate(): Call<List<PropertyDC>>
}