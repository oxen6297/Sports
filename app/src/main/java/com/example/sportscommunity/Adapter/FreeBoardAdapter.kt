package com.example.sportscommunity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.Content
import com.example.sportscommunity.databinding.CommunityItemListBinding

class FreeBoardAdapter(
    private val communityList: MutableList<Content>?
) : RecyclerView.Adapter<FreeBoardAdapter.ViewHolder>() {

    class ViewHolder(val binding: CommunityItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Content) {
            binding.com = data
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