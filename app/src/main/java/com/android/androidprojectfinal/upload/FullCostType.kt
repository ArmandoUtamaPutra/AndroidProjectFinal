package com.android.androidprojectfinal.upload

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.androidprojectfinal.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.full_cost_type.*
import java.io.IOException
import java.util.*

class FullCostType : AppCompatActivity() {

    val PERMISSION_REQUEST_CODE = 1001
    val PICK_IMAGE_REQUEST = 900;
    lateinit var filePath: Uri
    lateinit var imgView: ImageView
    var value = 0.0
    lateinit var storage : FirebaseStorage
    lateinit var storageReference : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_cost_type)
        imgView = findViewById(R.id.inputimage1)
        storage = FirebaseStorage.getInstance()
        storageReference =  storage.reference
        fct_choose.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            this@FullCostType, Manifest
                                .permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE)
                    } else {
                        chooseFile()
                    }
                }
                else -> chooseFile()
            }
        }
        btn_post1.setOnClickListener {
            uploadFile()
        }

    }

    private fun chooseFile() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(
                        this@FullCostType, "Oops! Permission Denied!!"
                        , Toast.LENGTH_SHORT
                    ).show()
                else
                    chooseFile()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imgView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadFile() {
        val progress = ProgressDialog(this).apply {
            setTitle("Uploading Picture....")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
        var ref: StorageReference = storageReference.child(
            "images/" + UUID.randomUUID()
                .toString()
        )
        ref.putFile(filePath)
            .addOnSuccessListener { taskSnapshot ->
                progress.dismiss()
                Toast.makeText(this@FullCostType, "Uploaded", Toast.LENGTH_SHORT).show();
            }
            .addOnFailureListener { e ->
                progress.dismiss()
                Toast.makeText(
                    this@FullCostType, "Failed" + e.message,
                    Toast.LENGTH_SHORT
                ).show();
            }.addOnProgressListener { taskSnapshot ->
                value = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                    .getTotalByteCount())
                progress.setMessage("Uploaded...." + value.toInt())

            }

    }
}


