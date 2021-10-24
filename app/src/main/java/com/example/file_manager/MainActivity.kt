package com.example.file_manager

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.file_manager.common.Constant

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(checkPermission()){
            Constant.path = Environment.getExternalStorageDirectory().path
        }
        else{
            requestPermission()
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
//đẩy lên git để tôi clone về xem sao