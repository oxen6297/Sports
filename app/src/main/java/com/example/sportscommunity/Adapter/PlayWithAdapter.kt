package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.PlayWithItemListBinding

class PlayWithAdapter(
    private val context: Context,
    private val playWith: MutableList<PlayWith>?,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<PlayWithAdapter.ViewHolder>(), Filterable {

    private var files = playWith
    private var unfiles = playWith

    inner class ViewHolder(val binding: PlayWithItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: PlayWith) {
            binding.alone = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayWithAdapter.ViewHolder {
        val binding =
            PlayWithItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayWithAdapter.ViewHolder, position: Int) {

        holder.onBind(playWith!![position])

        val item = playWith[position]

        holder.binding.aloneLayout.setOnClickListener {
            titleHash.put("title", item.title.toString())
            categoryHash.put("category", holder.binding.playCategory.text.toString())
            localHash.put("local", item.local.toString())
            descriptionHash.put("description", item.description.toString())
            genderHash.put("gender", item.gender.toString())
            minageHash.put("minage", item.minage.toString())
            maxageHash.put("maxage", item.maxage.toString())
            userImageHash.put("image", item.userimage.toString())
            nicknameHash.put("nickname", item.nickname.toString())
            writedateHash.put("writedate", item.writedate.toString())

            mainActivity.changeFragment(17)
        }

        when (playWith[position].id) {
            1 -> {
                holder.binding.playCategory.text = "구기종목"
            }
            2 -> {
                holder.binding.playCategory.text = "레져"
            }
            3 -> {
                holder.binding.playCategory.text = "해양 스포츠"
            }
            4 -> {
                holder.binding.playCategory.text = "생활 스포츠"
            }
            5 -> {
                holder.binding.playCategory.text = "동계 스포츠"
            }
            6 -> {
                holder.binding.playCategory.text = "E-스포츠"
            }
        }
    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                files = if (charString.isEmpty()) {
                    unfiles
                } else {
                    val filterList = mutableListOf<PlayWith>()
                    for (item in unfiles!!) {
                        if (item.title == charString) filterList.add(item)
                    }
                    filterList
                }
                val filterResult = FilterResults()
                filterResult.values = files
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                files = results?.values as MutableList<PlayWith>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = playWith!!.size
}