package com.example.file_manager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.file_manager.databinding.ActivitySearchResultBinding
import com.example.file_manager.fragment.listAllFile.AllFileAdapter
import timber.log.Timber

class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = AllFileAdapter(this){
            SearchResultViewModel.openFolder(it)
        }
        binding.rcvSearchResult.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvSearchResult.adapter = adapter

        SearchResultViewModel.resultFiles.observe(this, {
            adapter.listFile = it

            if (adapter.listFile.size == 0){
                binding.rcvSearchResult.visibility = View.GONE
                binding.tvNotFound.visibility = View.VISIBLE
            } else {
                binding.rcvSearchResult.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.GONE
            }
        })

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Timber.e("query completed")
                SearchResultViewModel.getSearchFile(p0!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val searchText = p0!!.lowercase()
                if (searchText.isNotEmpty()){
                    Timber.e("query change")
                    SearchResultViewModel.getSearchFile(searchText)
                }
                return true
            }

        })
    }

    override fun onDestroy() {
        SearchResultViewModel.clear()
        super.onDestroy()
    }
}