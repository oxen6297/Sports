package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.GroupPlay
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.GroupItemListBinding
import java.util.logging.Filter
import java.util.logging.LogRecord

class GroupAdapter(
    private val context: Context, private val groupList: MutableList<GroupPlay>?
) : RecyclerView.Adapter<GroupAdapter.ViewHolder>(),Filterable {

    private var files = groupList
    private var unfiles = groupList

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

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                files = if (charString.isEmpty()){
                    unfiles
                } else {
                    val filterList = mutableListOf<GroupPlay>()
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
                files = results?.values as MutableList<GroupPlay>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = groupList!!.size
}