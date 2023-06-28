package com.riezki.indopaynewsapp.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.withTransaction
import com.riezki.indopaynewsapp.model.local.entity.NewsEntity
import com.riezki.indopaynewsapp.model.local.room.NewsDatabase
import com.riezki.indopaynewsapp.network.api.ApiService
import com.riezki.indopaynewsapp.utils.Resource

class NewsRepository(private val apiService: ApiService, private val database: NewsDatabase) {

    fun getAllNews(): LiveData<Resource<List<NewsEntity>>> {
        return liveData {
            val oldData = database.newsDao().getAllNews()
            emit(Resource.Loading(data = oldData))
            try {
                val response = apiService.getHeadlineNews()
                val articles = response.articles
                val newsList = articles.map { news ->
                    NewsEntity(
                        id = news.id,
                        title = news.title,
                        publishedAt = news.publishedAt,
                        urlToImage = news.urlToImage,
                        url = news.url,
                        author = news.author,
                        description = news.description,
                    )
                }
                database.withTransaction {
                    database.newsDao().deleteAll()
                    database.newsDao().insertAllToLocal(newsList)
                }

            } catch (e: Exception) {
                Log.e("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
                emit(Resource.Error(statusCode = e.message.hashCode(), message = e.message.toString(), data = null))
            }

            emit(Resource.Success(data = database.newsDao().getAllNews()))
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(apiService: ApiService, database: NewsDatabase): NewsRepository {
            return instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, database)
            }.also {
                instance = it
            }
        }
    }
}