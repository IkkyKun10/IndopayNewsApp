package com.riezki.indopaynewsapp.model.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("articles")
    val articles: List<ArticleItems>,

    @field:SerializedName("status")
    val status: String? = null
)
