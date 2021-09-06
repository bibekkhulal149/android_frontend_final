package com.example.online_veg_store.Repository

import com.example.online_veg_store.InsertResponse
import com.example.online_veg_store.Response.ImageResponse
import com.example.online_veg_store.Response.LoginResponse
import com.example.online_veg_store.api.MyApiRequest
import com.example.online_veg_store.api.ServiceBuilder
import com.example.online_veg_store.api.api
import com.example.online_veg_store.entity.Register
import com.example.online_veg_store.entity.User
import okhttp3.MultipartBody

class UserRepo: MyApiRequest() {

    val api = ServiceBuilder.buildServices(api::class.java)


    suspend fun Login(username:String, password:String): LoginResponse {
        return apiRequest {

            api.Login(username, password)
        }

    }



    suspend fun register(user: Register): InsertResponse
    {
        return apiRequest {
            api.Register(user)
        }

    }

    suspend fun upload( id:String,body: MultipartBody.Part): ImageResponse {
        return apiRequest {
            api.addPhoto(id,body)
        }
    }



}
