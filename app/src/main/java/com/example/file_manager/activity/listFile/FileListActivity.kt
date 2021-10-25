package com.example.file_manager.activity.listFile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.file_manager.R
import com.example.file_manager.common.Constant
import com.example.file_manager.databinding.ActivityFileListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FileListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileListBinding
    private var isGrid = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayout = GridLayoutManager(applicationContext, 3, GridLayoutManager.VERTICAL, false)
        val listLayout = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rcvAllFile.layoutManager = listLayout
        Log.e("rcv", "layout")

        val files = ArrayList<File>()
        var path = intent.getStringExtra("path")
        if(path.isNullOrBlank()){
            path = Constant.path
        }
        lifecycleScope.launch(Dispatchers.IO) {
            File(path).listFiles()?.let {
                for (file in it) {
                    files.add(file)
                }
                Log.e("list file size", files.size.toString())
                binding.rcvAllFile.adapter = AllFileAdapter(applicationContext, files)
            }
            withContext(Dispatchers.Main){
                if(files.size == 0){
                    binding.txtNoFile.visibility = View.VISIBLE
                    binding.rcvAllFile.visibility = View.GONE
                }
            }
        }
        binding.imgLayoutChange.setOnClickListener {
            Log.e("click", "change")
            if (isGrid) {
                binding.imgLayoutChange.setImageResource(R.drawable.ic_grid_on)
                binding.rcvAllFile.layoutManager = listLayout
                isGrid = false
            } else {
                binding.imgLayoutChange.setImageResource(R.drawable.ic_list)
                binding.rcvAllFile.layoutManager = gridLayout
                isGrid = true
            }
        }

    }
}