package com.example.online_veg_store

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.se.omapi.Session
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.online_veg_store.Repository.UserRepo
import com.example.online_veg_store.api.ServiceBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity(),SensorEventListener {
    private lateinit var tvregister: Button
    private lateinit var btnlogin: Button
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    var sensor:Sensor?=null
    var sensorManager:SensorManager?=null



    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var linearLayout:LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (!hasPermission()) {
            requestPermission()
        }
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
       if(!checkSensor()){
           return
       }
        else{

           sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
           sensorManager!!.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)

       }



        btnlogin = findViewById(R.id.btnLogIn)
        etPassword = findViewById(R.id.etPassword)
        etUsername = findViewById(R.id.etUsername)
        tvregister = findViewById(R.id.tvRegister)

        btnlogin.setOnClickListener() {
            login()
        }
        tvregister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }



    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@LoginActivity,
            permissions, 1
        )
    }

    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }


    private fun checkSensor():Boolean{
        var flag = true
        if(sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null)
        {

            flag = false
        }
        else{


        }
        return flag
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
                        loginSharedPref()
                        ServiceBuilder.token = "Bearer " + response.token
                        ServiceBuilder.user= response.data

                        withContext(Main){
                            Toast.makeText(this@LoginActivity, "Logged In ", Toast.LENGTH_SHORT).show()
                        }
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                DashboardActivity::class.java
                            )
                        )
                        showHighPriorityNotification()


                    }

                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login error", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun checkRunTimePermission() {
        if (!hasPermission()) {
            requestPermission()
        }
    }



    private fun showHighPriorityNotification() {
        val notificationManager= NotificationManagerCompat.from(this)

        val notificationChannels=NotificationChannels(this)
        notificationChannels.createNotificationChannels()

        val notification= NotificationCompat.Builder(this,notificationChannels.Channel_1)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle("LOGGED IN")
            .setContentText("Login Successful!")
            .setColor(Color.BLUE)


            .build()

        notificationManager.notify(1,notification)
    }

    fun saveSharedPref() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val sharedPref = getSharedPreferences(
            "MyPref",
            MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
        Toast.makeText(
            this@LoginActivity,
            "Username and password saved",
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun onSensorChanged(event: SensorEvent?) {
        val values =event!!.values[0]
        if(values<-2){
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }
        else if(values>2){

            startActivity(Intent(this@LoginActivity,Store::class.java))

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    fun loginSharedPref() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val loginSharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = loginSharedPref.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.commit()
    }
    override fun onResume() {
        supportActionBar!!.hide()
        super.onResume()
    }
}