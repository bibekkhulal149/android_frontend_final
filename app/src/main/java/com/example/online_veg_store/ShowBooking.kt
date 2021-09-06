package com.example.online_veg_store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bookingAdapter
import com.example.online_veg_store.Repository.VegRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_booking)
        val rv: RecyclerView = findViewById(R.id.rvs)
        var lst:MutableList<Booking> = mutableListOf()


        CoroutineScope(Dispatchers.IO).launch{
            val repo = VegRepo()
            val response = repo.getBook()
            lst = response.data!!
            withContext(Dispatchers.Main){

                val adapter = bookingAdapter(this@ShowBooking,lst)
                rv.layoutManager = LinearLayoutManager(this@ShowBooking,LinearLayoutManager.VERTICAL,false)
                rv.adapter =adapter

            }



        }

    }
    override fun onResume() {
        supportActionBar!!.hide()
        super.onResume()
    }}