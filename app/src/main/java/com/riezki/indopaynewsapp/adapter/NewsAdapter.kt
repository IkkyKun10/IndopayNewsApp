package com.riezki.indopaynewsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.riezki.indopaynewsapp.R
import com.riezki.indopaynewsapp.databinding.ItemNewsLayoutBinding
import com.riezki.indopaynewsapp.model.local.entity.NewsEntity
import com.riezki.indopaynewsapp.utils.ObjectFormatter

class NewsAdapter(private val onClickToDetail: (NewsEntity) -> Unit) :
    ListAdapter<NewsEntity, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>() {

            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean =
                oldItem == newItem
        }
    }

    class ViewHolder(private val binding: ItemNewsLayoutBinding, val onClickToDetail: (NewsEntity) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsEntity) {
            with(binding) {
                imgHealine.load(item.urlToImage) {
                    crossfade(true)
                    placeholder(R.drawable.ic_downloading)
                    error(R.drawable.ic_error)
                }

                titleHeadline.text = item.title
                textDescription.text = item.description
                namaPenulisId.text = item.author
                tanggalTxt.text = ObjectFormatter.formatDate(item.publishedAt.toString())
                itemView.setOnClickListener { onClickToDetail(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = ItemNewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item, onClickToDetail)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}