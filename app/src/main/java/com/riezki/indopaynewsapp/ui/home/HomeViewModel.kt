package com.riezki.indopaynewsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.riezki.indopaynewsapp.model.local.entity.NewsEntity
import com.riezki.indopaynewsapp.network.NewsRepository
import com.riezki.indopaynewsapp.utils.Resource

class HomeViewModel(private val repository: NewsRepository) : ViewModel() {

    fun getHeadlineNews(category: String? = "general") : LiveData<Resource<List<NewsEntity>>> {
        return repository.getAllNews(category)
    }

}