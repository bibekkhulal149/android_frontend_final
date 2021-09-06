package com.example.online_veg_store

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.example.online_veg_store.Repository.VegRepo
import com.example.online_veg_store.entity.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddProduct : AppCompatActivity(), View.OnClickListener {
    private val gc = 0
    private val cc = 1
    var image: String? = null

    private lateinit var foodName: EditText
    private lateinit var description: EditText
    private lateinit var rating: EditText
    private lateinit var price: EditText

    private lateinit var images: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        foodName = findViewById(R.id.name)
        description = findViewById(R.id.description)
        rating = findViewById(R.id.rating)
        price = findViewById(R.id.price)
        var submit: Button = findViewById(R.id.submit)
        images = findViewById(R.id.image)
        images.setOnClickListener(this)
        submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.image->{
                val popup = PopupMenu(this, images)
                popup.menuInflater.inflate(R.menu.gallery_camera, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menuCamera ->
                            og()
                        R.id.menuGallery -> {
                            oc()
                        }


                    }

                    true
                }
                popup.show()
            }
            R.id.submit->{

if(validate()){
    CoroutineScope(Dispatchers.IO).launch {
        val ur = VegRepo()
        val food = Product(Name = foodName.text.toString(), Description = description.text.toString(), Rating = rating.text.toString(), Price = price.text.toString())

        val response = ur.insertFood(food)
        if (response.success == true) {

            uploadImage(response.data?._id!!.toString(), image.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddProduct, "Vegetable  Added", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddProduct, DashboardActivity::class.java))

            }


        }


    }

}

            }


        }
    }
    fun og() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, gc)

    }

    fun oc() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, cc)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == gc && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                image = cursor.getString(columnIndex)
                images.setImageBitmap(BitmapFactory.decodeFile(image))
                cursor.close()
            } else if (requestCode == cc && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                image = file!!.absolutePath
                images.setImageBitmap(BitmapFactory.decodeFile(image))
            }
        }

    }


    fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file?.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    fun uploadImage(id: String, image: String) {
        if (image != null) {

            var file = File(image!!)
            val extention = MimeTypeMap.getFileExtensionFromUrl(image)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
            var reqFile = RequestBody.create(MediaType.parse(mimeType), file)
            var body = MultipartBody.Part.createFormData("file", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val ur = VegRepo()
                    val response = ur.addPhoto(id,body)
                    if (response.status == true) {

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddProduct, "Vegetable Added", Toast.LENGTH_SHORT).show()

                        }

                    }

                } catch (ex: Exception) {

                }


            }

        }
    }
    fun validate(): Boolean
    {
        var flag = true
        var lst = mutableListOf<EditText>()
        lst.add(foodName)
        lst.add(price)
        lst.add(description)
        lst.add(rating)
        for(data in 0..lst.size - 1 )
        {
            if(lst[data].text.toString() == "")
            {
                lst[data].setError("Field Required")
                flag = false
                break
            }
        }
        return flag
    }
    override fun onResume() {
        supportActionBar!!.title = "Online Clothing Store"
        super.onResume()
    }
}