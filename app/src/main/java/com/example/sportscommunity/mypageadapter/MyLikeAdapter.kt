package com.example.sportscommunity.mypageadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.MyWrite
import com.example.sportscommunity.databinding.MyLikeItemListBinding
import com.example.sportscommunity.databinding.MyWriteItemListBinding

class MyLikeAdapter : RecyclerView.Adapter<MyLikeAdapter.ViewHolder>() {

    private var myLikeList = mutableListOf<MyWrite>()

    class ViewHolder(val binding: MyLikeItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: MyWrite) {
            binding.myLike = data
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getSetData(myWriteList: MutableList<MyWrite>) {
        this.myLikeList = myWriteList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MyLikeItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(myLikeList[position])
    }

    override fun getItemCount(): Int = myLikeList.size
}