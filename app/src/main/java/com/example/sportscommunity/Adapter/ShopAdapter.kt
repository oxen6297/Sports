package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.ShopListItemBinding

class ShopAdapter(
    private val context: Context,
    private val shopList: MutableList<Shop>?,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>(), Filterable {

    private var files = shopList
    private var unfiles = shopList

    class ViewHolder(val binding: ShopListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Shop) {
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
        val item = shopList[position]

        holder.binding.shopLayout.setOnClickListener {
            titleHash.put("title", item.title.toString())
            categoryHash.put("category", item.id.toString())
            localHash.put("local", item.local.toString())
            descriptionHash.put("description", item.description.toString())
            nicknameHash.put("nickname", item.nickname.toString())
            writedateHash.put("writedate", item.writedate.toString())
            shopImageHash.put("imageOne", item.usedimage1)
            shopImageHashTwo.put("imageTwo", item.usedimage2)
            shopImageHashThree.put("imageThree", item.usedimage3)
            shopImageHashFour.put("imageFour", item.usedimage4)
            shopImageHashFive.put("imageFive", item.usedimage5)
            priceHash.put("price", item.price.toString())
            userImageHash.put("userimage",item.userimage.toString())

            mainActivity.changeFragment(18)
        }
    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                files = if (charString.isEmpty()) {
                    unfiles
                } else {
                    val filterList = mutableListOf<Shop>()
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
                files = results?.values as MutableList<Shop>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = shopList!!.size
}