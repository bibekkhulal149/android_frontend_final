package com.example.vegetable

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.online_veg_store.Repository.UserRepo
import com.example.online_veg_store.api.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : WearableActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnlogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnlogin = findViewById(R.id.btnLogIn)
        etPassword = findViewById(R.id.etPassword)
        etUsername = findViewById(R.id.etUsername)

        btnlogin.setOnClickListener() {
            login()
        }
    }







    private fun login() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Please enter your username")
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password")
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = UserRepo()
                    val response = repository.Login(username, password)
                    if (response.success == true) {
                        ServiceBuilder.token = "Bearer " + response.token
                        ServiceBuilder.user= response.data
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@MainActivity, "asdasdasd ", Toast.LENGTH_SHORT).show()
                        }
                        startActivity(
                            Intent(
                                this@MainActivity,
                                DashboardActivity::class.java
                            )
                        )


                    }

                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Login error", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }



}