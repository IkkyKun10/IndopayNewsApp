package com.riezki.indopaynewsapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.riezki.indopaynewsapp.adapter.NewsAdapter
import com.riezki.indopaynewsapp.databinding.ActivityHomeBinding
import com.riezki.indopaynewsapp.ui.detail.DetailActivity
import com.riezki.indopaynewsapp.utils.Resource
import com.riezki.indopaynewsapp.utils.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        newsAdapter = NewsAdapter { news ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.INTENT_TO_DETAIL, news)
            startActivity(intent)
        }
        setRecyclerView()

        showNewsHeadline()
    }

    private fun showNewsHeadline() {
        viewModel.getHeadlineNews().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBarRv.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBarRv.visibility = View.GONE
                        newsAdapter.submitList(result.data)
                    }

                    is Resource.Error -> {
                        binding.progressBarRv.visibility = View.GONE
                        Toast.makeText(this, "Error get data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    
    private fun setRecyclerView() {
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = newsAdapter
            clipToPadding = false
        }

    }
}