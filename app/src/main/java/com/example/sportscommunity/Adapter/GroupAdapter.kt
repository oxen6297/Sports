package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.GroupItemListBinding

class GroupAdapter(
    private val context: Context,
    private val groupList: MutableList<GroupPlay>?,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    class ViewHolder(val binding: GroupItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: GroupPlay) {
            binding.group = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GroupItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(groupList!![position])
        val item = groupList[position]

        Glide.with(context).load(groupList[position].titleimage).centerCrop()
            .into(holder.binding.groupItemImage)

        holder.binding.numberMember.text =
            groupList[position].peoplenownum.toString() + "/" + groupList[position].peoplenum.toString()

        holder.binding.groupLayout.setOnClickListener {
            titleHash.put("title", item.title.toString())
            categoryHash.put("category", holder.binding.groupItemName.text.toString())
            onceHash.put("once", item.once.toString())
            localHash.put("local", item.local.toString())
            lineHash.put("line", item.line.toString())
            descriptionHash.put("description", item.description.toString())
            nownumHash.put("nownum", item.peoplenownum.toString())
            peoplenumHash.put("peoplenum", item.peoplenum.toString())
            genderHash.put("gender", item.gender.toString())
            minageHash.put("minage", item.minage.toString())
            maxageHash.put("maxage", item.maxage.toString())
            titleImageHash.put("image", item.titleimage.toString())

            mainActivity.changeFragment(16)
        }

        when (groupList[position].id) {
            1 -> {
                holder.binding.groupItemName.text = "구기종목"
            }
            2 -> {
                holder.binding.groupItemName.text = "레져"
            }
            3 -> {
                holder.binding.groupItemName.text = "해양 스포츠"
            }
            4 -> {
                holder.binding.groupItemName.text = "생활 스포츠"
            }
            5 -> {
                holder.binding.groupItemName.text = "동계 스포츠"
            }
            6 -> {
                holder.binding.groupItemName.text = "E-스포츠"
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    override fun getItemCount(): Int = groupList!!.size
}