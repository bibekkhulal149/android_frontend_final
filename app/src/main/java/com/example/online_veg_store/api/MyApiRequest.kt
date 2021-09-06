package com.example.online_veg_store.api

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

//10.0.2.2:3000 in virtual machine
abstract class MyApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()//call garni kam garxa
        if (response.isSuccessful) {//body ko response
            return response.body()!!
        } else {
            //   val error: String = response.errorBody().toString()
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("success"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error code : ${response.code()}")
            throw IOException(message.toString())
        }
    }
}