package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.databinding.NewsItemListBinding

class ListSourceAdapter(
    private val context: Context, private val newsData: MutableList<News>?
) : RecyclerView.Adapter<ListSourceAdapter.ViewHolder>() {

    class ViewHolder(val binding: NewsItemListBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(data:News){
            binding.news = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NewsItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(newsData!![position])
        holder.binding.run {

            //텍스트 밑줄
            newsTitle.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            //뉴스 제목 클릭시 사이트로 이동
            newsTitle.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("${news?.url}"))
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = newsData!!.size
}