package com.example.online_veg_store.Response

import com.example.online_veg_store.entity.User

data class ImageResponse(
    val success:Boolean?=null,
    val data: User?=null,
    val message:String?=null

) {
}