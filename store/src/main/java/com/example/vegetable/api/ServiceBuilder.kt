package com.example.online_veg_store.api

import com.example.online_veg_store.entity.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
      val  BASE_URL= "http://192.168.137.132:3000/"
    var token:String?=null
    var user:User?=null
    private val okhttp = OkHttpClient.Builder()
    private val retroFitBuilder: Retrofit.Builder = Retrofit.Builder().baseUrl(BASE_URL).
    addConverterFactory(GsonConverterFactory.create())
        .client(okhttp.build())
     private val retrofit: Retrofit = retroFitBuilder.build()
    //generic functions
    fun <T> buildServices(serviceType:Class<T>):T{
        return retrofit.create(serviceType)
    }
}