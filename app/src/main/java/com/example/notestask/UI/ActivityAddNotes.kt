package com.example.notestask.UI

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.notestask.BaseActivity
import com.example.notestask.BuildConfig
import com.example.notestask.Helpers.toast
import com.example.notestask.R
import com.example.notestask.RoomDb.AddNotes
import com.example.notestask.RoomDb.DataBase
import com.example.notestask.databinding.ActivityAddNotesBinding
import kotlinx.coroutines.launch
import java.io.File

class ActivityAddNotes : BaseActivity() {
    private val FINAL_TAKE_PHOTO = 1
    private val FINAL_CHOOSE_PHOTO = 2
    private var picture: ImageView? = null
    private var imageUri: Uri? = null
    lateinit var binding: ActivityAddNotesBinding
    var  imagePath: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val takePhoto: Button = findViewById(R.id.cameraButton)
        val chooseFromAlbum: Button = findViewById(R.id.galleryButton)
        picture = findViewById(R.id.picture)

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val des = binding.etDes.text.toString()
            when {
                binding.etDes.text.toString().isEmpty() -> {
                    applicationContext.toast("Please Enter Description")
                }
                binding.etTitle.text.toString().isEmpty() -> {
                    applicationContext.toast("Please Enter Title")
                }
                else -> {

                    launch {

                        applicationContext?.let {
                            if (imagePath.toString().isBlank()||imagePath.isEmpty()){
                                val data=AddNotes(imageUri.toString(), title, des)
                                DataBase(it).getlogin().addNotes(data)
                                Log.e("insertcamera", data.toString())
                            }else{
                                var data = AddNotes(imagePath, title, des)
                                DataBase(it).getlogin().addNotes(data)

                                Log.e("insertgallery", data.toString())
                            }

                            val intent = Intent(it, ActivityLogin::class.java)
                            startActivity(intent)
                            it.toast("Note added successfully")
                        }
                    }
                }
            }
        }

        takePhoto.setOnClickListener{
            val outputImage = File(externalCacheDir, "output_image.jpg")
            if(outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if(Build.VERSION.SDK_INT >= 24){
                FileProvider.getUriForFile(this,  BuildConfig.APPLICATION_ID + ".provider", outputImage)
            } else {
                Uri.fromFile(outputImage)
            }

            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, FINAL_TAKE_PHOTO)
        }

        chooseFromAlbum.setOnClickListener{
            val checkSelfPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
            else{
                openAlbum()
            }
        }
    }

    private fun openAlbum(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, FINAL_CHOOSE_PHOTO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 ->
                if (grantResults.isNotEmpty() && grantResults.get(0) ==PackageManager.PERMISSION_GRANTED){
                    openAlbum()
                }
                else {
                    Toast.makeText(this, "You deied the permission", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(imageUri?.let {
                        getContentResolver().openInputStream(
                            it
                        )
                    })
                    picture!!.setImageBitmap(bitmap)
                }
            FINAL_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data)
                    }
                    else{
                        handleImageBeforeKitkat(data)
                    }
                }
        }
    }


    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?) {
        val uri = data!!.data
        if (DocumentsContract.isDocumentUri(this, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if (uri != null) {
                if ("com.android.providers.media.documents" == uri.authority){
                    val id = docId.split(":")[1]
                    val selsetion = MediaStore.Images.Media._ID + "=" + id
                    imagePath = imagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selsetion)
                } else if ("com.android.providers.downloads.documents" == uri.authority){
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                    imagePath = imagePath(contentUri, null)
                }
            }
        }
        else if (uri != null) {
            if ("content".equals(uri.scheme, ignoreCase = true)){
                imagePath = imagePath(uri, null)
            }
            else if ("file".equals(uri.scheme, ignoreCase = true)){
                imagePath = uri.path.toString()
            }
        }
        displayImage(imagePath)
    }

    private fun handleImageBeforeKitkat(data: Intent?) {}

    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = uri?.let { contentResolver.query(it, null, selection, null, null ) }
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    private fun displayImage(imagePath: String?) {
        if (this.imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(this.imagePath)
            picture?.setImageBitmap(bitmap)
            Log.e("path",imagePath.toString())
        }
        else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show()
        }
    }
}