package com.example.file_manager.fragment.listAllFile

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.file_manager.R
import com.example.file_manager.databinding.ItemFileBinding
import java.io.File
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.file_manager.BuildConfig
import com.example.file_manager.common.Constant
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class AllFileAdapter(private var onItemClick: (String) -> Unit)
    : RecyclerView.Adapter<AllFileAdapter.AllFileViewHolder>(), Filterable {
    var listFile = ArrayList<File>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var mlistFile = listFile
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
                root.setOnClickListener {
                    if (file.isDirectory) onItemClick(file.path)
                    else{
                        openFile(file, binding.root.context)
                    }
                }
                root.setOnLongClickListener {
                    val filePath = file.absolutePath
                    val intentShare = Intent(Intent.ACTION_VIEW)
                    intentShare.type = "application/docx"
                    true
                }
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
    private fun openFile(file: File, context: Context) {
        val url = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID+ ".provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (checkIfFileHasExtension(url.toString(), Constant.wordEx)) {
            Timber.d("Open word document")
            intent.setDataAndType(url, "application/msword")
        } else if (checkIfFileHasExtension(url.toString(), Constant.pdfEx)) {
            Timber.d("Open PDF file")
            intent.setDataAndType(url, "application/pdf")
        } else if (checkIfFileHasExtension(url.toString(), Constant.pptEx)) {
            Timber.d("Open Powerpoint file")
            intent.setDataAndType(url, "application/vnd.ms-powerpoint")
        } else if (checkIfFileHasExtension(url.toString(), Constant.excelEx)) {
            Timber.d("Open Excel file")
            intent.setDataAndType(url, "application/vnd.ms-excel")
        } else if (checkIfFileHasExtension(url.toString(), Constant.compressEx)) {
            Timber.d("Open zip file")
        } else if (checkIfFileHasExtension(url.toString(), Constant.musicEx)) {
            Timber.d("Open audio file")
            intent.setDataAndType(url, "audio/x-wav")
        } else if (checkIfFileHasExtension(url.toString(), Constant.imageEx)){
            Timber.d("Open image file")
            intent.setDataAndType(url, "image/*")
        } else if (checkIfFileHasExtension(url.toString(), Constant.plainTextEx)) {
            Timber.d("Open plain text file")
            intent.setDataAndType(url, "text/plain")
        } else if (checkIfFileHasExtension(url.toString(), Constant.gifEx)){
            Timber.d("Open gif file")
            intent.setDataAndType(url, "image/gif")
        }
        else if (checkIfFileHasExtension(url.toString(), Constant.videoEx)) {
            Timber.d("Open video file")
            intent.setDataAndType(url, "video/*")
        } else {
            Timber.d("Open other files")
            intent.setDataAndType(url, "*/*")
        }
        try {
            if (file.exists()) context.startActivity(
                Intent.createChooser(intent,"Open")
            ) else Toast.makeText(context, "File is corrupted", Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            Toast.makeText(context,"No Application is found to open this file. The File is saved at", Toast.LENGTH_LONG).show()
        }
    }
    private fun checkIfFileHasExtension(s: String, extend: MutableList<String>): Boolean {
        extend.forEach {
            if (s.endsWith(it))
                return true
        }
        return false
    }

    override fun getFilter(): Filter {
        return fileFilter
    }

    private val fileFilter = object : Filter(){
        override fun performFiltering(p0: CharSequence?): FilterResults {
            var filterList: ArrayList<File> = ArrayList()
            if(p0 == null || p0.isEmpty()){
                filterList = mlistFile
            }else{
                val query = p0.toString().trim().toLowerCase()
                mlistFile.forEach{
                    if(it.name.toLowerCase(Locale.ROOT).contains(query)) {
                        filterList.add(it)
                    }
                }
            }
            val p1 = FilterResults()
            p1.values = filterList
            return p1
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            if(p1?.values is ArrayList<*>){
                listFile.clear()
                listFile = p1.values as ArrayList<File>
                notifyDataSetChanged()
            }
        }

    }
}