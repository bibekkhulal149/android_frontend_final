package com.example.online_veg_store

import com.example.online_veg_store.entity.User

data class InsertResponse(
    val status:Boolean?=null,
    val data: User?=null

) {
}