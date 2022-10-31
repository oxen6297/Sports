package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ListSourceAdapter(
    private val context: Context, private val newsList: MutableList<News>?
) : RecyclerView.Adapter<ListSourceAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsItem: TextView = itemView.findViewById(R.id.news_title)
        val newsImg: ImageView = itemView.findViewById(R.id.news_img)
        val newsDescription: TextView = itemView.findViewById(R.id.news_description)
        val newsTime: TextView = itemView.findViewById(R.id.news_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = newsList!![position]
        val url = model.url

        Glide.with(context).load(model.urlToImage).error(R.drawable.picture).centerCrop()
            .into(holder.newsImg)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val timeText = sdf.parse(model.publishedAt)!!.toString()

        holder.newsItem.text = model.title
        holder.newsItem.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        holder.newsDescription.text = model.description
        holder.newsTime.text = timeText
        Log.d("paaaa",timeText)


        holder.newsItem.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$url"))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = newsList!!.size
}