package com.riezki.indopaynewsapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riezki.indopaynewsapp.di.Injection
import com.riezki.indopaynewsapp.network.NewsRepository
import com.riezki.indopaynewsapp.ui.home.HomeViewModel

class ViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) : ViewModelFactory {
            return ViewModelFactory(Injection.provideInjection(context))
        }
    }
}

