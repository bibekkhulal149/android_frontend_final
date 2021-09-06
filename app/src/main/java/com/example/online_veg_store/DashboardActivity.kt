package com.example.online_veg_store

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.example.online_veg_store.api.ServiceBuilder

class DashboardActivity : AppCompatActivity(), View.OnClickListener,SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val product:CardView= findViewById(R.id.product)
        val store:CardView= findViewById(R.id.store)
        val cart:CardView= findViewById(R.id.cart)
        val map:CardView= findViewById(R.id.map)
        val log:CardView= findViewById(R.id.logout)
        val prof:CardView= findViewById(R.id.profile)








        product.setOnClickListener(this)
                store.setOnClickListener(this)
        cart.setOnClickListener(this)
        map.setOnClickListener(this)
        log.setOnClickListener(this)
        prof.setOnClickListener(this)





    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.product->startActivity(Intent(this@DashboardActivity,AddProduct::class.java))
            R.id.store->startActivity(Intent(this@DashboardActivity,Store::class.java))
            R.id.cart->startActivity(Intent(this@DashboardActivity,ShowBooking::class.java))
            R.id.map->startActivity(Intent(this@DashboardActivity,MapsActivity::class.java))
            R.id.logout->{
                ServiceBuilder.token = null
                ServiceBuilder.user = null
                val prefs = getSharedPreferences("data", MODE_PRIVATE);
                val editor = prefs?.edit()
                if (editor != null) {
                    editor.clear()
                    editor.apply()
                }

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;

                finish();

                startActivity(Intent(this@DashboardActivity,LoginActivity::class.java))

            }
            R.id.profile->startActivity(Intent(this@DashboardActivity,Profile::class.java))




        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        val values = event!!.values[0]


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onResume() {
        supportActionBar!!.hide()
        super.onResume()
    }
}
