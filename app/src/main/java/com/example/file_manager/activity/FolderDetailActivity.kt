package com.example.file_manager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.file_manager.databinding.ActivityFolderDetailBinding
import com.example.file_manager.fragment.listAllFile.FileListViewModel
import com.example.file_manager.inf.OnBackPressed
import java.util.*

class FolderDetailActivity : AppCompatActivity() {
    lateinit var onBackPressed: OnBackPressed
    private lateinit var binding: ActivityFolderDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("typefolder")?.let{
            FileListViewModel.updateTypeOfFolder(it)
            binding.tvHeader.text = it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

        binding.searchBar.setOnClickListener {

        }

    }
    override fun onBackPressed() {

        if(onBackPressed.isClosed()){
            super.onBackPressed()
        }
        else{
            onBackPressed.onClick()
        }

    }

}