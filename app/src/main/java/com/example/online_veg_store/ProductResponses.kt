package com.example.online_veg_store

import com.example.online_veg_store.entity.Product

data class ProductResponses(

    val success:Boolean?=null,
    val data: Product?=null

) {
}