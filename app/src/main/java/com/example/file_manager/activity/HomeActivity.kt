package com.example.file_manager.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.file_manager.common.Constant
import com.example.file_manager.databinding.ActivityHomeBinding
import timber.log.Timber

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.d("Check permission")
        if(checkPermission()){
            Constant.path = Environment.getExternalStorageDirectory().path
            Constant.pathDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        }
        else{
            requestPermission()
        }

        with(binding){
            val intent = Intent(this@HomeActivity, FolderDetailActivity::class.java)
            btnOtherFolder.setOnClickListener {
                intent.putExtra("typefolder", "other")
                startActivity(intent)
            }
            btnImageFolder.setOnClickListener {
                intent.putExtra("typefolder", "image")
                startActivity(intent)
            }
            btnAudioFolder.setOnClickListener {
                intent.putExtra("typefolder", "audio")
                startActivity(intent)
            }
            btnVideoFolder.setOnClickListener {
                intent.putExtra("typefolder", "video")
                startActivity(intent)
            }
            btnDocumentFolder.setOnClickListener {
                intent.putExtra("typefolder", "document")
                startActivity(intent)
            }
            btnDownloadFolder.setOnClickListener {
                intent.putExtra("typefolder", "download")
                startActivity(intent)
            }

        }
    }


    private fun checkPermission(): Boolean{
        val res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(res == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }
    private fun requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this, "Please Allow", Toast.LENGTH_SHORT).show()
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
    }
}