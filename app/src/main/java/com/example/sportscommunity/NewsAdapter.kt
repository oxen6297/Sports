package com.example.sportscommunity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListSourceAdapter(
    val context: Context, val webSite: WebSite
) : RecyclerView.Adapter<ListSourceAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsItem: TextView = itemView.findViewById(R.id.news_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.newsItem.text = webSite.sources!![position].name
    }

    override fun getItemCount(): Int = webSite.sources!!.size
}