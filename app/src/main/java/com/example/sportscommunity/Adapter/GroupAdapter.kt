package com.example.sportscommunity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.GroupPlay
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.GroupItemListBinding

class GroupAdapter(
    private val context: Context, private val groupList: MutableList<GroupPlay>?
) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    class ViewHolder(val binding: GroupItemListBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun onBind(data:GroupPlay){
                binding.group = data
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GroupItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(groupList!![position])

    }

    override fun getItemCount(): Int = groupList!!.size
}