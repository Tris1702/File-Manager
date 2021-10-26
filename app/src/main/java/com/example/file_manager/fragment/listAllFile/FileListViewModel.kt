package com.example.file_manager.fragment.listAllFile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.file_manager.common.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class FileListViewModel : ViewModel() {
    val stackPath = Stack<String>()
    private val _files: MutableLiveData< ArrayList<File> > = MutableLiveData()
    val files: LiveData<ArrayList<File>> = _files

    private val _isGrid: MutableLiveData<Boolean> = MutableLiveData()
    val isGrid: LiveData<Boolean> = _isGrid

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

    init {
        stackPath.push(Constant.path)
        getFiles(stackPath.peek())
        _isGrid.postValue(true)
    }

}