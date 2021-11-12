package com.example.file_manager.fragment.listAllFile

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.file_manager.R
import com.example.file_manager.activity.FolderDetailActivity
import com.example.file_manager.inf.OnBackPressed
import com.example.file_manager.databinding.FragmentFileListBinding
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.File


class FileListFragment : Fragment(), OnBackPressed {
    private lateinit var binding: FragmentFileListBinding
    val gridLayout = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
    val listLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.plant(DebugTree())
        binding = FragmentFileListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AllFileAdapter(requireContext()){
            FileListViewModel.openFolder(it)
        }

        /**
         setting class handle menu mode

         **/
        adapter.setHandleMenuMode(object :ClassHandleMenuMode(){
            override fun changeMenuMode() {
                if(FileListViewModel.menuMode == MenuMode.OPEN)
                {
                    binding.menu.visibility = View.VISIBLE
                    FileListViewModel.menuMode = MenuMode.SELECT
                    binding.btnAddFolder.visibility = View.GONE
                }
                else
                {
                    binding.menu.visibility = View.GONE
                    FileListViewModel.menuMode = MenuMode.OPEN
                    binding.btnAddFolder.visibility = View.VISIBLE
                }
            }
        })

        binding.btnCopy.setOnClickListener{
            FileListViewModel.copy()
        }

        binding.btnPaste.setOnClickListener {
            FileListViewModel.paste()
        }

        binding.btnShare.setOnClickListener {
            context?.let {
                FileListViewModel.share(it)
            }
        }


        binding.rcvAllFile.layoutManager = listLayout

        binding.rcvAllFile.adapter = adapter
        FileListViewModel.files.observe(viewLifecycleOwner){
            adapter.listFile = it
            if (it.isNotEmpty()){
                Timber.d("It has files")
                FileListViewModel.changeStateLoading()
                binding.rcvAllFile.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
                binding.txtNoFile.visibility = View.GONE
            }
            else{
                FileListViewModel.changeStateLoading()
                Timber.d("It has no files")
                binding.txtNoFile.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
                binding.rcvAllFile.visibility = View.GONE
            }
        }

        (activity as FolderDetailActivity).onBackPressed = this

        binding.imgLayoutChange.setOnClickListener{
            FileListViewModel.changeLayout()
            FileListViewModel.isGrid.observe(viewLifecycleOwner){
                if (it) {
                    Timber.d("change to list")
                    binding.imgLayoutChange.setImageResource(R.drawable.ic_grid_on)
                    binding.rcvAllFile.layoutManager = listLayout
                }
                else {
                    Timber.d("change to grid")
                    binding.imgLayoutChange.setImageResource(R.drawable.ic_list)
                    binding.rcvAllFile.layoutManager = gridLayout
                }
            }
        }
        FileListViewModel.stateLoading.observe(viewLifecycleOwner){
            if (it){
                binding.loading.visibility = View.VISIBLE
                binding.rcvAllFile.visibility = View.GONE
                binding.txtNoFile.visibility = View.GONE
            }
        }
    }

    override fun onClick() {
        Timber.d("Clicked back")
        FileListViewModel.onBackPressed()
    }

    override fun isClosed(): Boolean {
        if (FileListViewModel.isRoot()){
            FileListViewModel.clear()
            return true
        }
        return false
    }

}