package com.example.online_veg_store.entity

import androidx.room.Entity
import  androidx.room.PrimaryKey


@Entity
data class User(


    val _id :String?=null,
                 var FirstName:String?=null,
                 var Lastname:String?=null,
                 var PhoneNumber :String?=null,
                 var Username :String?=null,
                 var Password :String?=null,
                 var Profile:String?=null,

)
