package com.example.sportscommunity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.Shop
import com.example.sportscommunity.databinding.ShopListItemBinding

class ShopAdapter(
    private val context: Context, private val shopList: MutableList<Shop>?
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ShopListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ShopListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = shopList!!.size
}