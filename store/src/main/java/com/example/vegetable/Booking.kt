package com.example.vegetable

import com.example.online_veg_store.entity.Product
import com.example.online_veg_store.entity.User


data class Booking(
    val _id:String?=null,
    val User:User?=null,
    val Qty:Int?=null,
    val Vegetables:Product?=null,
    val Date:String?=null


)