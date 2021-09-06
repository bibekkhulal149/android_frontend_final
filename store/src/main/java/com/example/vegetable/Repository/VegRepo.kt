package com.example.online_veg_store.Repository

import com.example.online_veg_store.*
import com.example.online_veg_store.api.MyApiRequest
import com.example.online_veg_store.api.ServiceBuilder
import com.example.online_veg_store.api.Vegetable
import com.example.online_veg_store.entity.Product
import com.example.vegetable.BookingResponse
import com.example.vegetable.DeleteResponse
import com.example.vegetable.books
import okhttp3.MultipartBody

class VegRepo:MyApiRequest() {

    var api = ServiceBuilder.buildServices(Vegetable::class.java)




    suspend fun addToCart(pid:String,Qty: books): DeleteResponse {

        return apiRequest {
            api.addToCart(pid,ServiceBuilder.token!!,Qty)
        }

    }


    suspend fun getBook(): BookingResponse {
        return apiRequest {
            api.getBooking(ServiceBuilder.token!!)
        }
    }



    suspend fun delete(id:String): DeleteResponse {
        return apiRequest {
            api.deleteBooking(ServiceBuilder.token!!,id)
        }
    }

    suspend fun update(id:String,qty: books): DeleteResponse {
        return apiRequest {
            api.update(ServiceBuilder.token!!,id,qty)
        }
    }






}