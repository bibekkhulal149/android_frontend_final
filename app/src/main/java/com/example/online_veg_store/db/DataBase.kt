package com.example.online_veg_store.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.online_veg_store.dao.UserDAO
import com.example.online_veg_store.entity.Product
import com.example.online_veg_store.entity.User


@Database(
    entities = [(Product::class)],
    version = 1
)
abstract class DataBase:RoomDatabase() {
    abstract fun getUserDAO(): UserDAO


    companion object{
        @Volatile
        private var instance: DataBase?=null
        fun getInstance(context: Context): DataBase{
            if(instance == null){
                synchronized(DataBase::class.java){
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }
        private  fun buildDatabase(context: Context):DataBase{
            return Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                "OnlineVegStore"
            ).build()
        }
    }
}


