package com.example.myapplication.presentation.ui.news.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.local.entity.news.ArticleEntity
import com.example.myapplication.presentation.ui.main.adapter.HistoryAdapter

class NewsAdapter(
    private val context: Context
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var news: List<ArticleEntity> = emptyList()

    fun setData(news: List<ArticleEntity>) {
        this.news = news
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_news,
            parent, false
        )
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = news[position]
        Glide.with(context).load(currentItem.urlToImage).into(holder.image)
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
    }

    override fun getItemCount(): Int {
        return news.size
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.news_image)
        val title: TextView = itemView.findViewById(R.id.news_title)
        val description: TextView = itemView.findViewById(R.id.news_description)

    }


}