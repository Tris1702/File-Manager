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
import java.io.File

class AllFileAdapter(private val context: Context?, private var files: ArrayList<File>)
    : RecyclerView.Adapter<AllFileAdapter.AllFileViewHolder>() {

    inner class AllFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgFile: ImageView = itemView.findViewById(R.id.img_icon_file)
        var tvName: TextView = itemView.findViewById(R.id.txt_file_name)

        fun bind(file: File){
            tvName.text = file.name
            if(file.isDirectory){
                imgFile.setImageResource(R.drawable.ic_folder)
            }
            else{
                imgFile.setImageResource(R.drawable.ic_normal_file)
            }
        }
    }

    override fun onBindViewHolder(holder: AllFileViewHolder, position: Int) {
        holder.tvName.text = files[position].name
        if(files[position].isDirectory){
            holder.imgFile.setImageResource(R.drawable.ic_folder)
        }
        else{
            holder.imgFile.setImageResource(R.drawable.ic_normal_file)
        }
        holder.itemView.setOnClickListener {
            if(files[position].isDirectory){
                val path = files[position].absolutePath
                var intent = Intent(context, FileListActivity::class.java)
                intent.putExtra("path", path)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFileViewHolder {
        return AllFileViewHolder((LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)) as View)
    }
}