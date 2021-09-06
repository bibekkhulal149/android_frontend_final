package com.example.online_veg_store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.online_veg_store.Repository.UserRepo
import com.example.online_veg_store.api.ServiceBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private var etUsername=""
    private var etPassword=""
    private lateinit var linearLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getSharedPref()
        if (etUsername==""){
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)
                startActivity(
                    Intent(
                        this@SplashActivity,
                        LoginActivity::class.java
                    )
                )
            }
        }
        else{
            login()
        }



    }
    private fun getSharedPref(){
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        etUsername = sharedPref.getString("username", "").toString()
        etPassword = sharedPref.getString("password", "").toString()
    }

    private fun login() {
        val username = etUsername
        val password = etPassword
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepo()
                val response = repository.Login(username, password)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer " + response.token
                    ServiceBuilder.user = response.data!!
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            DashboardActivity::class.java
                        )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        val snack =
                            Snackbar.make(
                                linearLayout,
                                "Invalid credentials",
                                Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK", View.OnClickListener {

                        })
                        snack.show()
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SplashActivity,
                        "Login error", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        supportActionBar!!.hide()
        super.onResume()
    }
}