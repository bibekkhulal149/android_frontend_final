
@file:Suppress("Annotator")



package com.example.online_veg_store.api

import com.example.online_veg_store.InsertResponse
import com.example.online_veg_store.Response.ImageResponse
import com.example.online_veg_store.Response.LoginResponse
import com.example.online_veg_store.entity.Register
import com.example.online_veg_store.entity.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface api {

    @POST("/insert")
    suspend fun Register(@Body user:Register):Response<InsertResponse>

    @FormUrlEncoded
    @POST("/login")
    suspend fun Login(@Field("username") username:String,
                      @Field("password") password:String):Response<LoginResponse>


    @Multipart
    @PUT("/photo/{id}")
    suspend fun addPhoto(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ):Response<ImageResponse>



}