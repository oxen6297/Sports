package com.example.sportscommunity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.ShopListItemBinding

class ShopAdapter(
    private val context: Context,
    private var shopList: MutableList<Shop>?,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>(),Filterable {

    var filterShopList: MutableList<Shop>? = shopList

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
        val item = shopList!![position]

        holder.binding.shopLayout.setOnClickListener {
            titleHash["title"] = item.title.toString()
            categoryHash["category"] = item.id.toString()
            localHash["local"] = item.local.toString()
            descriptionHash["description"] = item.description.toString()
            nicknameHash["nickname"] = item.nickname.toString()
            writedateHash["writedate"] = item.writedate.toString()
            shopImageHash["imageOne"] = item.usedimage1
            shopImageHashTwo["imageTwo"] = item.usedimage2
            shopImageHashThree["imageThree"] = item.usedimage3
            shopImageHashFour["imageFour"] = item.usedimage4
            shopImageHashFive["imageFive"] = item.usedimage5
            priceHash["price"] = item.price.toString()
            userImageHash["userimage"] = item.userimage.toString()
            ShopBoardId["shop"] = item.usedid.toString()

            mainActivity.changeFragment(18)
        }
    }


    override fun getItemCount(): Int = shopList?.size?:0

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().replace(" ","")
                shopList = if (charString.isEmpty()) {
                    filterShopList
                } else {
                    val filterList = mutableListOf<Shop>()
                    if (filterShopList != null) {
                        for (item in filterShopList!!) {
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
                filterResults.values = shopList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                shopList = results?.values as MutableList<Shop>?
                notifyDataSetChanged()
            }
        }
    }
}