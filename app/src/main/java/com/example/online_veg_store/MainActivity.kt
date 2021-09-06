package com.example.online_veg_store

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.*
import com.example.online_veg_store.Repository.UserRepo
import com.example.online_veg_store.entity.Register
import com.example.online_veg_store.entity.User
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

class MainActivity : AppCompatActivity() {
    lateinit var etFirstName: EditText
    lateinit var etLastName:EditText

    lateinit var img: ImageView

    lateinit var etUsername: EditText
    lateinit var etPhone: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword:EditText
    lateinit var etRepeatPassword:EditText
    lateinit var register:Button
//    lateinit var register2:Button

    private val gallery_code=0
    private val camera_code=1
    var image:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etFirstName = findViewById(R.id.etFname)
        etLastName = findViewById(R.id.etLname)
        etUsername = findViewById(R.id.etUsername)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etRepeatPassword = findViewById(R.id.etCPassword)
        register= findViewById(R.id.btnReg)
        
        img = findViewById(R.id.imagess)


        register.setOnClickListener(){

            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val username = etUsername.text.toString()
            val address = etAddress.text.toString()
            val phone = etPhone.text.toString()
            val password = etPassword.text.toString()
            val repeatPassword = etRepeatPassword.text.toString()

            if(password!=repeatPassword)
            {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = Register(fname =firstName,lname=lastName,address=address,phone=phone,username=username,password=password,type = "Customer")

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val    userRepository = UserRepo()
                        val response = userRepository.register(user)
                        if(image!=null)
                        {
                           uploadImage(response.data!!._id!!,image!!)

                        }
                        if (response.status == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Register bhayo", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "$ex Username cannot be duplicate", Toast.LENGTH_SHORT
                            ).show()


                        }
                    }
                }
            }
        }



        img.setOnClickListener(){
            val popup = PopupMenu(this,img)
            popup.menuInflater.inflate(R.menu.gallery_camera,popup.menu)
            popup.setOnMenuItemClickListener { item->
                when(item.itemId){
                    R.id.menuCamera->
                        openCamera()
                    R.id.menuGallery->{
                        openGallery()
                    }


                }

                true
            }
            popup.show()
        }


//        register2.setOnClickListener(){
//            Toast.makeText(this@MainActivity, "asdasdasdas", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
//
//        }





     }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,gallery_code)

    }

    fun openCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,camera_code)


    }


    fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                this@MainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

    fun uploadImage(id: String,image:String) {
        if(image!=null)
        {

            var file = File(image!!)
            val extention = MimeTypeMap.getFileExtensionFromUrl(image)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
            var reqFile = RequestBody.create(MediaType.parse(mimeType), file)
 var body = MultipartBody.Part.createFormData("file",file.name,reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val ur = UserRepo()
                    val response = ur.upload(id,body)
                    if(response.success==true)
                    { withContext(Dispatchers.Main){
                        startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                            Toast.makeText(this@MainActivity, "Add in Room databse", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                catch(ex:Exception)
                {

                }



            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == gallery_code && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                image = cursor.getString(columnIndex)
                img.setImageBitmap(BitmapFactory.decodeFile(image))
                cursor.close()
            } else if (requestCode == camera_code && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                image = file!!.absolutePath
                img.setImageBitmap(BitmapFactory.decodeFile(image))
            }
        }
    }



    override fun onResume() {
        supportActionBar!!.hide()
        super.onResume()
    }


}
