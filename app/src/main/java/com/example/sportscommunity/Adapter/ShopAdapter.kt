package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.GroupPlay
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.Shop
import com.example.sportscommunity.databinding.ShopListItemBinding

class ShopAdapter(
    private val context: Context, private val shopList: MutableList<Shop>?
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>(),Filterable {

    private lateinit var itemClickListener: OnItemClickListener
    private var files = shopList
    private var unfiles = shopList

    class ViewHolder(val binding: ShopListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data:Shop){
            binding.shop = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ShopListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(shopList!![position])

        holder.binding.shopLayout.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                files = if (charString.isEmpty()){
                    unfiles
                } else {
                    val filterList = mutableListOf<Shop>()
                    for (item in unfiles!!){
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
                files = results?.values as MutableList<Shop>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = shopList!!.size

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}