package com.example.file_manager.fragment.listAllFile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.file_manager.BuildConfig
import com.example.file_manager.common.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.io.path.copyTo

object FileListViewModel : ViewModel() {
    val stackPath = Stack<String>()
    private val _files: MutableLiveData< ArrayList<File> > = MutableLiveData()
    val files: LiveData<ArrayList<File>> = _files

    private val _isGrid: MutableLiveData<Boolean> = MutableLiveData()
    val isGrid: LiveData<Boolean> = _isGrid

    /**
     Variable copy - paste file
     **/
    var selectedFile: File? = null
    var currentDictionary: File? = null
    var menuMode: MenuMode = MenuMode.OPEN
    var handleMode: HandleMode = HandleMode.NONE

    fun isRoot(): Boolean{
        return stackPath.size == 1
    }

    fun openFolder(path: String){

        stackPath.push(path)
        getFiles(path)
    }

    fun onBackPressed(){
        stackPath.pop()
        getFiles(stackPath.peek())
    }

    fun changeLayout(){
        _isGrid.postValue(!isGrid.value!!)
    }

    private fun getFiles(path: String) {

        viewModelScope.launch(Dispatchers.IO){
            val listFiles = ArrayList<File>()
            File(path).listFiles()?.let{
                for(file in it){
                    listFiles.add(file)
                }
            }
            _files.postValue(listFiles)
        }
    }

    /**
    Function copy - paste file
     **/
    fun share(context: Context){
        //TODO: This is just */*
        val shareIntent = Intent().apply{
            this.action = Intent.ACTION_SEND
            this.type = "*/*"
        }
        val uriFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", selectedFile!!)
        } else{
            Uri.fromFile(selectedFile!!)
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriFile)
        startActivity(context, shareIntent, null)
    }
    fun cut()
    {

    }

    fun copy()
    {
        handleMode = HandleMode.COPY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun paste()
    {
        if(handleMode == HandleMode.COPY)
        {
            val targetPath = currentDictionary?.toString() + "/" +selectedFile?.nameWithoutExtension +"."+ selectedFile?.extension
            val target = Paths.get(targetPath)
            val destPath = selectedFile?.absolutePath
            val dest = Paths.get(destPath)
            dest.copyTo(target,false)

            val newFile:File = File(targetPath)
            _files.value?.add(newFile)
            _files.postValue(_files.value)

        }
        handleMode = HandleMode.NONE

    }


    init {
        stackPath.push(Constant.path)
        getFiles(stackPath.peek())
        _isGrid.postValue(true)
    }

}

