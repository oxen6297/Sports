package com.example.sportscommunity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.AloneItemListBinding

class AloneAdapter(
    private val context: Context,
    private val aloneList: MutableList<PlayWith>?,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<AloneAdapter.ViewHolder>() {

    class ViewHolder(val binding: AloneItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: PlayWith) {
            binding.alone = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AloneItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(aloneList!![position])
        val item = aloneList[position]

        Glide.with(context).load(aloneList[position].userimage).centerCrop()
            .into(holder.binding.aloneItemImage)

        holder.binding.aloneLayout.setOnClickListener {
            titleHash.put("title", item.title.toString())
            categoryHash.put("category", holder.binding.aloneItemName.text.toString())
            localHash.put("local", item.local.toString())
            descriptionHash.put("description", item.description.toString())
            genderHash.put("gender", item.gender.toString())
            minageHash.put("minage", item.minage.toString())
            maxageHash.put("maxage", item.maxage.toString())
            userImageHash.put("image", item.userimage.toString())
            nicknameHash.put("nickname", item.nickname.toString())
            writedateHash.put("writedate", item.writedate.toString())
            AloneBoardId.put("alone",item.individualid.toString())
            AloneCategoryHash.put("categoryid",item.id.toString())


            mainActivity.changeFragment(17)
        }

        when (aloneList[position].id) {
            1 -> {
                holder.binding.aloneItemName.text = "구기종목"
            }
            2 -> {
                holder.binding.aloneItemName.text = "레져"
            }
            3 -> {
                holder.binding.aloneItemName.text = "해양 스포츠"
            }
            4 -> {
                holder.binding.aloneItemName.text = "생활 스포츠"
            }
            5 -> {
                holder.binding.aloneItemName.text = "동계 스포츠"
            }
            6 -> {
                holder.binding.aloneItemName.text = "E-스포츠"
            }
        }
    }

    override fun getItemCount(): Int = aloneList?.size?:0
}