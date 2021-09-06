package com.example.online_veg_store.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.online_veg_store.NotificationChannels
import com.example.online_veg_store.R
import com.example.online_veg_store.Repository.VegRepo
import com.example.online_veg_store.api.ServiceBuilder
import com.example.online_veg_store.books
import com.example.online_veg_store.entity.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class productAdapter(val context: Context, val lst:MutableList<Product>): RecyclerView.Adapter<productAdapter.productViewHolder>() {
        var productList = emptyList<Product>()
        inner class productViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val ivImage: ImageView
            val tvName: TextView
            val tvPrice : TextView
            val desc:TextView
            val btn: Button


            init {
                ivImage = itemView.findViewById(R.id.pc_imageview)
                tvName = itemView.findViewById(R.id.pc_pname)
                tvPrice = itemView.findViewById(R.id.pc_price)
                desc = itemView.findViewById(R.id.pc_weight)
                btn = itemView.findViewById(R.id.btnaddtocart)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vegetables, parent, false)
            return productViewHolder(view)
        }

        override fun onBindViewHolder(holder: productViewHolder, position: Int) {
            val product = lst[position]
//
            holder.tvName.text = product.Name
            holder.tvPrice.text = "Rs. ${product.Price}"
holder.btn.setOnClickListener(){
dialog(product)
}

            if(product.Image.equals("No-Image")){
               Glide.with(context).load("https://media.istockphoto.com/photos/car-engine-disassembled-many-parts-picture-id1139697726?s=612x612").into(holder.ivImage)

            }
            else{
                Glide.with(context).load("${ServiceBuilder.BASE_URL}images/${product.Image}").into(holder.ivImage)


            }

        }

    override fun getItemCount(): Int {
        return lst.size
    }


    fun dialog(food:Product){
        val d = AlertDialog.Builder(context)
        d.setTitle("Book Confirmation")

        d.setMessage("Are you sure you want to book this item?")
        d.setPositiveButton("Yes"){dialog,which->

            book(food._id!!)
            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
            dialog.cancel()

        }
        d.setNegativeButton("No"){dialog, which ->

        }
        val alert = d.create()

        alert.setCancelable(true)
        alert.show()


    }
    fun book(pid:String){


        CoroutineScope(Dispatchers.IO).launch{
            val repo = VegRepo()
            val response = repo.addToCart(pid, books(1))
            if(response.success==true){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
                }
            }
            showLowPriorityNotification()






        }



    }

    private fun showLowPriorityNotification() {
        val notificationManager= NotificationManagerCompat.from(context)

        val notificationChannels= NotificationChannels(context)
        notificationChannels.createNotificationChannels()

        val notification= NotificationCompat.Builder(context,notificationChannels.Channel_2)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle("Cart Notification")
            .setContentText("Item Added To Cart Successfully!")
            .setColor(Color.BLUE)
            .build()

        notificationManager.notify(2,notification)
    }

}
