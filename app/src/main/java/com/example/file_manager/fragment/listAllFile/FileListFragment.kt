package com.example.file_manager.fragment.listAllFile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.file_manager.MainActivity
import com.example.file_manager.R
import com.example.file_manager.inf.OnBackPressed
import com.example.file_manager.databinding.FragmentFileListBinding
import timber.log.Timber
import timber.log.Timber.DebugTree


class FileListFragment : Fragment(), OnBackPressed, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private lateinit var binding: FragmentFileListBinding
    private val viewModel: FileListViewModel by lazy { FileListViewModel() }
    val gridLayout = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
    val listLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    val adapter = AllFileAdapter{
        viewModel.openFolder(it)
    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvAllFile.layoutManager = listLayout

        binding.rcvAllFile.adapter = adapter
        viewModel.files.observe(viewLifecycleOwner){
            adapter.listFile = it
            if (it.isNotEmpty()){
                Timber.d("It has files")
                binding.txtNoFile.visibility = View.INVISIBLE
            }
            else{
                Timber.d("It has no files")
                binding.txtNoFile.visibility = View.VISIBLE
            }
        }

        (activity as MainActivity).onBackPressed = this

        binding.imgLayoutChange.setOnClickListener{
            viewModel.changeLayout()
            viewModel.isGrid.observe(viewLifecycleOwner){
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
    }

    override fun onClick() {
        Timber.d("Clicked back")
        viewModel.onBackPressed()
    }

    override fun isClosed(): Boolean {
        return viewModel.isRoot()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_view, menu)
        var searchView = menu.findItem(R.menu.search_view).actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }

    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        return true
    }

}