package com.example.online_veg_store

import com.example.online_veg_store.entity.Product

data class ProductResponse (
    val success:Boolean?=null,
    val data:MutableList<Product>?=null

        ){
}