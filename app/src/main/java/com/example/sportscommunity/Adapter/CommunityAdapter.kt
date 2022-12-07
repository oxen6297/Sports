package com.example.sportscommunity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.Content
import com.example.sportscommunity.GroupPlay
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.CommunityItemListBinding
import com.example.sportscommunity.databinding.GroupItemListBinding

class CommunityAdapter(
    private val context: Context, private val communityList: MutableList<Content>?
) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    class ViewHolder(val binding: CommunityItemListBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun onBind(data:Content){
            binding.community = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CommunityItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(communityList!![position])

    }

    override fun getItemCount(): Int = communityList!!.size
}