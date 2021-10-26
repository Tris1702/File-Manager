package com.example.file_manager.activity.listFile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.file_manager.R
import com.example.file_manager.databinding.ItemFileBinding
import java.io.File

class AllFileAdapter(private var onItemClick: (String) -> Unit)
    : RecyclerView.Adapter<AllFileAdapter.AllFileViewHolder>() {
    var listFile = ArrayList<File>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class AllFileViewHolder(private val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File) {
            with(binding){
                txtFileName.text = file.name
                if(file.isDirectory){
                    imgIconFile.setImageResource(R.drawable.ic_folder)
                }
                else {
                    imgIconFile.setImageResource(R.drawable.ic_normal_file)
                }
            }
            binding.root.setOnClickListener {
                onItemClick(file.path)
            }
        }

    }

    override fun onBindViewHolder(holder: AllFileViewHolder, position: Int) {
        holder.bind(listFile[position])
    }

    override fun getItemCount(): Int {
        return listFile.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFileViewHolder {
        val binding = ItemFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllFileViewHolder(binding)
    }
}