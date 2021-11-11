package com.example.file_manager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.example.file_manager.MainActivity
import com.example.file_manager.R
import com.example.file_manager.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnOtherFolder.setOnClickListener {
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            }
        }
    }
}