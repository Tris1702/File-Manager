package com.example.file_manager.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.file_manager.BuildConfig
import com.example.file_manager.R
import com.example.file_manager.common.Constant
import com.example.file_manager.databinding.ItemFileBinding
import com.example.file_manager.fragment.listAllFile.FileListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

class SearchResultAdapter(var context: Context, private var onItemClick: (String) -> Unit) : RecyclerView.Adapter<SearchResultAdapter.SearchResultHolder>(){
    lateinit var path: String
    var listResultFiles = ArrayList<File>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class SearchResultHolder(private val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(file: File){
            path = file.path
            with(binding){
                txtFileName.text = file.name
                GlobalScope.launch(Dispatchers.Main) {
                    if (file.isDirectory){
                        imgIconFile.setImageResource(R.drawable.ic_folder)
                    } else{
                        when {
                            checkIfFileHasExtension(file.name, Constant.musicEx) -> {
                                imgIconFile.setImageResource(R.drawable.ic_audio)
                            }
                            checkIfFileHasExtension(file.name, Constant.imageEx) -> {
                                imgIconFile.setImageResource(R.drawable.ic_image)
                            }
                            checkIfFileHasExtension(file.name, Constant.videoEx) -> {
                                imgIconFile.setImageResource(R.drawable.ic_video)
                            }
                            else -> {
                                imgIconFile.setImageResource(R.drawable.ic_document)
                            }
                        }
                    }
                }
                root.setOnClickListener {
                    if (file.isDirectory) {
                        FileListViewModel.currentDictionary = file
                        onItemClick(file.path)
                    } else {
                        openFile(file, binding.root.context)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultHolder {
        var binding = ItemFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        holder.bind(listResultFiles[position])
    }

    override fun getItemCount(): Int {
        return listResultFiles.size
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

        GlobalScope.launch(Dispatchers.IO) {
            when {
                checkIfFileHasExtension(url.toString(), Constant.wordEx) -> {
                    Timber.d("Open word document")
                    intent.setDataAndType(url, "application/msword")
                }
                checkIfFileHasExtension(url.toString(), Constant.pdfEx) -> {
                    Timber.d("Open PDF file")
                    intent.setDataAndType(url, "application/pdf")
                }
                checkIfFileHasExtension(url.toString(), Constant.pptEx) -> {
                    Timber.d("Open Powerpoint file")
                    intent.setDataAndType(url, "application/vnd.ms-powerpoint")
                }
                checkIfFileHasExtension(url.toString(), Constant.excelEx) -> {
                    Timber.d("Open Excel file")
                    intent.setDataAndType(url, "application/vnd.ms-excel")
                }
                checkIfFileHasExtension(url.toString(), Constant.compressEx) -> {
                    Timber.d("Open zip file")
                }
                checkIfFileHasExtension(url.toString(), Constant.musicEx) -> {
                    Timber.d("Open audio file")
                    intent.setDataAndType(url, "audio/x-wav")
                }
                checkIfFileHasExtension(url.toString(), Constant.imageEx) -> {
                    Timber.d("Open image file")
                    intent.setDataAndType(url, "image/*")
                }
                checkIfFileHasExtension(url.toString(), Constant.plainTextEx) -> {
                    Timber.d("Open plain text file")
                    intent.setDataAndType(url, "text/plain")
                }
                checkIfFileHasExtension(url.toString(), Constant.gifEx) -> {
                    Timber.d("Open gif file")
                    intent.setDataAndType(url, "image/gif")
                }
                checkIfFileHasExtension(url.toString(), Constant.videoEx) -> {
                    Timber.d("Open video file")
                    intent.setDataAndType(url, "video/*")
                }
                else -> {
                    Timber.d("Open other files")
                    intent.setDataAndType(url, "*/*")
                }
            }
            try {
                if (file.exists()) context.startActivity(
                    Intent.createChooser(intent, "Open with")
                ) else Toast.makeText(context, "File is corrupted", Toast.LENGTH_LONG).show()
            } catch (ex: Exception) {
                Toast.makeText(
                    context,
                    "No Application is found to open this file. The File is saved at",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun checkIfFileHasExtension(s: String, extend: MutableList<String>): Boolean {
        return withContext(Dispatchers.IO){
            extend.forEach {
                if (s.endsWith(it))
                    return@withContext true
            }
            false
        }
    }
}