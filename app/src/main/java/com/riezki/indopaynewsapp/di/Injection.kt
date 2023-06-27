package com.riezki.indopaynewsapp.di

import android.content.Context
import com.riezki.indopaynewsapp.model.local.room.NewsDatabase
import com.riezki.indopaynewsapp.network.NewsRepository
import com.riezki.indopaynewsapp.network.api.ApiConfig

object Injection {

    fun provideInjection(context: Context) : NewsRepository {
        val apiService = ApiConfig.apiService(context)
        val database = NewsDatabase.getInstance(context)
        return NewsRepository.getInstance(apiService, database)
    }
}