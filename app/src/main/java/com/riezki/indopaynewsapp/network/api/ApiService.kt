package com.riezki.indopaynewsapp.network.api

import com.riezki.indopaynewsapp.model.response.NewsResponse
import com.riezki.indopaynewsapp.utils.Constraint
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getHeadlineNews(
        @Query(COUNTRY) countryCode: String = "id",
        @Query(PAGE) pageNumber: Int = 1,
        @Query(SIZE) pageSize: Int = 20,
        @Query(API_KEYS) apiKey: String = Constraint.API_KEY
    ) : NewsResponse


    companion object {
        const val COUNTRY = "country"
        const val PAGE = "page"
        const val SIZE = "pageSize"
        const val API_KEYS = "apiKey"
    }
}