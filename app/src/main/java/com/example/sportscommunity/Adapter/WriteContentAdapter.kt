package com.example.sportscommunity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.Content
import com.example.sportscommunity.databinding.WriteContentLayoutBinding

class WriteContentAdapter(
    private val context: Context, private val contentList: MutableList<Content>?
) : RecyclerView.Adapter<WriteContentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: WriteContentLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WriteContentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = contentList!!.size
}