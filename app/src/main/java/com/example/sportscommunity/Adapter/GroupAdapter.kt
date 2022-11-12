package com.example.sportscommunity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.Group
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.GroupItemListBinding

class GroupAdapter(
    private val context: Context, private val groupList: MutableList<Group>?
) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: GroupItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GroupItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.run {
            val model = groupList!![position]
            val url = model.groupImage

            Glide.with(context).load(url).error(R.drawable.picture).fitCenter()
                .into(groupItemImage)

            groupItemName.text = model.category
            groupItemTime.text = model.time
        }
    }

    override fun getItemCount(): Int = groupList!!.size
}