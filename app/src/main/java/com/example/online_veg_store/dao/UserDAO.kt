package com.example.online_veg_store.dao

import com.example.online_veg_store.entity.User
import androidx.room.*
import com.example.online_veg_store.entity.Product

@Dao
interface UserDAO {

    @Insert
    suspend fun registerUser(user:Product)

    @Query( "select * from Product")
    suspend fun checkUser():MutableList<Product>

    @Query("Delete from Product")
    suspend fun delete()
}