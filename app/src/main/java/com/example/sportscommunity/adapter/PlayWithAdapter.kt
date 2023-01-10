package com.example.sportscommunity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.PlayWithItemListBinding

class PlayWithAdapter(
    private val context: Context,
    private var playWith: MutableList<PlayWith>?,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<PlayWithAdapter.ViewHolder>(),Filterable {

    var filterPlayWithList:MutableList<PlayWith>? = playWith

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

        val item = playWith!![position]

        holder.binding.aloneLayout.setOnClickListener {
            titleHash["title"] = item.title.toString()
            categoryHash["category"] = holder.binding.playCategory.text.toString()
            localHash["local"] = item.local.toString()
            descriptionHash["description"] = item.description.toString()
            genderHash["gender"] = item.gender.toString()
            minageHash["minage"] = item.minage.toString()
            maxageHash["maxage"] = item.maxage.toString()
            userImageHash["image"] = item.userimage.toString()
            nicknameHash["nickname"] = item.nickname.toString()
            writedateHash["writedate"] = item.writedate.toString()
            AloneBoardId["alone"] = item.individualid.toString()
            AloneCategoryHash["categoryid"] = item.id.toString()

            mainActivity.changeFragment(17)
        }

        when (playWith!![position].id) {
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

    override fun getItemCount(): Int = playWith?.size?:0

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().replace(" ","")
                playWith = if (charString.isEmpty()) {
                    filterPlayWithList
                } else {
                    val filterList = mutableListOf<PlayWith>()
                    if (filterPlayWithList != null) {
                        for (item in filterPlayWithList!!) {
                            if (item.title!!.replace(" ", "")
                                    .contains(charString) || item.nickname?.replace(" ", "")
                                    ?.contains(charString)!!
                            ) {
                                filterList.add(item)
                            }
                        }
                    }
                    filterList
                }
                val filterResults = FilterResults()
                filterResults.values = playWith
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                playWith = results?.values as MutableList<PlayWith>?
                notifyDataSetChanged()
            }
        }
    }
}