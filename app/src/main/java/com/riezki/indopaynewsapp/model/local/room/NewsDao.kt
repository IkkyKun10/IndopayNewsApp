package com.riezki.indopaynewsapp.model.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riezki.indopaynewsapp.model.local.entity.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllToLocal(news: List<NewsEntity>)

    @Query("DELETE FROM news_entity")
    suspend fun deleteAll()

    @Query("SELECT * FROM news_entity")
    fun getAllNews() : List<NewsEntity>
}