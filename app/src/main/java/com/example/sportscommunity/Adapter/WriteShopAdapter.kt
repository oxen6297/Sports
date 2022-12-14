package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.WriteShopListItemBinding

class WriteShopAdapter(
    val context: Context,
    private val imageList: MutableList<Uri>,
    private val imageText: MutableList<String>
) : RecyclerView.Adapter<WriteShopAdapter.ViewHolder>() {

    private lateinit var itemClickListener: OnItemClickListener
    private val imageUri = "drawable://" + R.drawable.add_image_background

    class ViewHolder(val binding: WriteShopListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WriteShopListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = imageList.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = imageList[position]
        holder.binding.imageText.text = imageText[position]
        Glide.with(context).load(imageItem).centerCrop().into(holder.binding.listImg)

        holder.binding.listImg.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.binding.listImg.setOnLongClickListener {
            holder.binding.deleteBtn.visibility = View.VISIBLE
            true
        }

        holder.binding.deleteBtn.setOnClickListener {
            imageList[position] = Uri.parse(imageUri)
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}