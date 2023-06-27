package com.riezki.indopaynewsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.riezki.indopaynewsapp.databinding.ItemNewsLayoutBinding
import com.riezki.indopaynewsapp.model.local.entity.NewsEntity

class NewsAdapter(private val onClickToDetail: (NewsEntity) -> Unit) :
    ListAdapter<NewsEntity, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean =
                oldItem == newItem

        }
    }

    class ViewHolder(private val binding: ItemNewsLayoutBinding, val onClickToDetail: (NewsEntity) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsEntity) {
            with(binding) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = ItemNewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item, onClickToDetail)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}