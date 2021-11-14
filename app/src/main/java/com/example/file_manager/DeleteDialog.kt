package com.example.file_manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.file_manager.databinding.DeleteDirectoryBinding
import com.example.file_manager.fragment.listAllFile.FileListViewModel
import java.io.File

class DeleteDialog : DialogFragment(){
    private lateinit var binding:DeleteDirectoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DeleteDirectoryBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            FileListViewModel.delete()
            dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        FileListViewModel.selectedFile = File("")
    }


}