package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.databinding.NewsItemListBinding

class ListSourceAdapter(
    private val context: Context, private val newsList: MutableList<News>?
) : RecyclerView.Adapter<ListSourceAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: NewsItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NewsItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.run {
            val model = newsList!![position]
            val url = model.url

            Glide.with(context).load(model.urlToImage).error(R.drawable.picture).centerCrop()
                .into(newsImg)

            newsTitle.text = model.title
            newsTitle.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            newsTitle.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$url"))
                context.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int = newsList!!.size
}