package com.example.online_veg_store.api


import com.example.online_veg_store.*
import com.example.online_veg_store.entity.Product
import com.example.vegetable.BookingResponse
import com.example.vegetable.DeleteResponse
import com.example.vegetable.books
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface Vegetable {





    @POST("/booking/{pid}")
    suspend fun addToCart(@Path("pid") id:String, @Header("Authorization") token:String, @Body Qty: books): Response<DeleteResponse>

    @GET("/booking/show")
    suspend fun getBooking(@Header("Authorization")token:String ): Response<BookingResponse>

    @DELETE("/delete/{id}")
    suspend fun deleteBooking(@Header("Authorization")token:String, @Path("id")id:String?): Response<DeleteResponse>
    @PUT("/updateBooking/{bid}")
    suspend fun update(@Header("Authorization")token:String, @Path("bid")id:String?, @Body qty:books): Response<DeleteResponse>




}