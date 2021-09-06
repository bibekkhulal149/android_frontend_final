package com.example.online_veg_store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.online_veg_store.api.ServiceBuilder
import com.example.online_veg_store.entity.User
import de.hdodenhof.circleimageview.CircleImageView

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)





        val fname: TextView = findViewById(R.id.fname)
        val lname: TextView = findViewById(R.id.lname)
        val phone: TextView = findViewById(R.id.phone)
        val username: TextView = findViewById(R.id.username)
        val img: CircleImageView = findViewById(R.id.profile)


        fname.text =  ServiceBuilder.user!!.FirstName.toString()
        lname.text =  ServiceBuilder.user!!.Lastname.toString()
        username.text=  ServiceBuilder.user!!.Username.toString()
        phone.text =  ServiceBuilder.user!!.PhoneNumber.toString()


        if( ServiceBuilder.user!!.Profile=="no-img.jpg"){
            Glide.with(this).load(R.drawable.ic_person).into(img)

        }
        else{

            Glide.with(this).load("${ServiceBuilder.BASE_URL}images/${ ServiceBuilder.user!!.Profile.toString()}").into(img)
        }
    }
    override fun onResume() {
        supportActionBar!!.hide()
        super.onResume()
    }

}