package com.example.online_veg_store

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.online_veg_store.Repository.VegRepo
import com.example.online_veg_store.adapter.productAdapter
import com.example.online_veg_store.db.DataBase
import com.example.online_veg_store.entity.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Store : AppCompatActivity(),SensorEventListener {
    var sensorManager: SensorManager?=null
    var sensor:Sensor?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        val rv:RecyclerView = findViewById(R.id.rv)
        var lst:MutableList<Product> = mutableListOf()

        sensorManager=getSystemService(SENSOR_SERVICE) as SensorManager
        if (!checkSensor()){
            return
        }
        else{
            sensor=sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }


        CoroutineScope(Dispatchers.IO).launch {


            val ur = VegRepo()

            val response = ur.getFood()
            var list:MutableList<Product> = mutableListOf()
            list = response.data!!
            lst = DataBase.getInstance(this@Store).getUserDAO().checkUser()

            if(lst.size<list.size){

                lst.clear()
                DataBase.getInstance(this@Store).getUserDAO().delete()
                for(data in list){

                    DataBase.getInstance(this@Store).getUserDAO().registerUser(data)
                }

                lst = DataBase.getInstance(this@Store).getUserDAO().checkUser()

            }



            withContext(Dispatchers.Main){
                val rv: RecyclerView = findViewById(R.id.rv)
                val adapter = productAdapter(this@Store,lst)

                rv.layoutManager = LinearLayoutManager(this@Store)
                rv.adapter = adapter


            }

        }




    }

    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.System.canWrite(this)) {
//                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
//                intent.data = Uri.parse("package:" + getPackageName())
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//            } else {
//
//
//                Settings.System.putInt(
//                    contentResolver,
//                    Settings.System.SCREEN_BRIGHTNESS, 0
//                )
//                if(values>15000f) {
//                    Toast.makeText(this, "15000", Toast.LENGTH_SHORT).show()
//                    Settings.System.putInt(
//                        contentResolver,
//                        Settings.System.SCREEN_BRIGHTNESS, 80
//                    )
//                }
//                else if(values> 30000f){
//                    Toast.makeText(this, "30000", Toast.LENGTH_SHORT).show()
//
//                    Settings.System.putInt(
//                        contentResolver,
//                        Settings.System.SCREEN_BRIGHTNESS, 190
//                    )
//                }
//                else if(values== 40000f){
//                    Toast.makeText(this, "40000", Toast.LENGTH_SHORT).show()
//
//                    Settings.System.putInt(
//                        contentResolver,
//                        Settings.System.SCREEN_BRIGHTNESS, 255
//                    )
//                }
//                else{
//
//                }
//            }
//        }
//        else{}
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        supportActionBar!!.title = "Online Clothing Store"
        super.onResume()
    }
}

