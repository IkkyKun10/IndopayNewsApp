package com.riezki.indopaynewsapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.riezki.indopaynewsapp.R
import com.riezki.indopaynewsapp.databinding.ActivityDetailBinding
import com.riezki.indopaynewsapp.model.local.entity.NewsEntity

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val news = intent.getParcelableExtra<NewsEntity>(INTENT_TO_DETAIL)

        binding.webView.apply {
            webViewClient = WebViewClient()
            news?.url?.let { loadUrl(it) }
        }
    }

    companion object {
        const val INTENT_TO_DETAIL = "intent_to_detail"
    }
}