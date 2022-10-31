package com.example.sportscommunity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListSourceAdapter(
    private val context: Context, private val newsList: MutableList<News>?
) : RecyclerView.Adapter<ListSourceAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsItem: TextView = itemView.findViewById(R.id.news_item)
        val newsItem2: ImageView = itemView.findViewById(R.id.news_item2)
        val newsItem3: TextView = itemView.findViewById(R.id.news_item3)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = newsList!![position]

        Glide.with(context).load(model.urlToImage).centerCrop().into(holder.newsItem2)

        holder.newsItem.text = model.title
        holder.newsItem3.text = model.description

    }

    override fun getItemCount(): Int = newsList!!.size
}