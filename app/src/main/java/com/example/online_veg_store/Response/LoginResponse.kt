package com.example.online_veg_store.Response

import com.example.online_veg_store.entity.User

data class LoginResponse(
    val success:Boolean?=null,
    val token:String?=null,
    val data: User?=null
)
