package com.example.file_manager.fragment.listAllFile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.file_manager.R
import com.example.file_manager.databinding.ItemFileBinding
import java.io.File
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.file_manager.BuildConfig
import com.example.file_manager.common.Constant
import timber.log.Timber
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


class AllFileAdapter(private var onItemClick: (String) -> Unit)
    : RecyclerView.Adapter<AllFileAdapter.AllFileViewHolder>() {

   lateinit var path : String

    /**
     class handle MenuMode
     */
    var handleMenuMode: ClassHandleMenuMode? = null
    @JvmName("setHandleMenuMode1")
    fun setHandleMenuMode(handleMenuMode: ClassHandleMenuMode)
    {
        this.handleMenuMode = handleMenuMode
    }

    var listFile = ArrayList<File>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class AllFileViewHolder(private val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(file: File) {
            path = file.path
            with(binding){
                txtFileName.text = file.name
                if(file.isDirectory){
                    imgIconFile.setImageResource(R.drawable.ic_folder)
                }
                else {
                    imgIconFile.setImageResource(R.drawable.ic_normal_file)
                }

                root.setOnClickListener {
                    if (file.isDirectory) {
                        FileListViewModel.currentDictionary = file
                        onItemClick(file.path)
                    }
                    else{
                        openFile(file, binding.root.context)
                    }
                }


                /*
                Handle long click
                long click -> view 3 options: move, share, delete
                In this branch, handle only move
                 */
                root.setOnLongClickListener {
                    FileListViewModel.selectedFile = file
                    handleMenuMode?.changeMenuMode()
                    true
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun Path.exists() : Boolean = Files.exists(this)

        @RequiresApi(Build.VERSION_CODES.O)
        private fun Path.isFile() : Boolean = !Files.isDirectory(this)

        @RequiresApi(Build.VERSION_CODES.O)
        fun Path.move(dest : Path, overwrite : Boolean = false) : Boolean {
            return if(isFile()){
                if(dest.exists()){
                    if(overwrite){
                        //Perform the move operation. REPLACE_EXISTING is needed for
                        //replacing a file
                        Files.move(this, dest, StandardCopyOption.REPLACE_EXISTING)
                        Timber.d("move file true ")
                        true
                    } else {
                        Timber.d("move file false ")
                        false
                    }
                } else {
                    //Perform the move operation
                    Files.move(this, dest)
                    Timber.d("move file true ")
                    true
                }
            } else {
                false
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        Timber.d("-----"+file.absolutePath+"-----")
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
}
