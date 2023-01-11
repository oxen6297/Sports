package com.example.sportscommunity.mypageadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.MyWrite
import com.example.sportscommunity.databinding.MyWriteItemListBinding

class MyGroupWriteAdapter : RecyclerView.Adapter<MyGroupWriteAdapter.ViewHolder>() {

    private var myWriteList = mutableListOf<MyWrite>()

    class ViewHolder(val binding: MyWriteItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data:MyWrite) {
            binding.myWrite = data
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getSetData(myWriteList: MutableList<MyWrite>) {
        this.myWriteList = myWriteList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MyWriteItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(myWriteList[position])
    }

    override fun getItemCount(): Int = myWriteList.size
}