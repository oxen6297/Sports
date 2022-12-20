package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.Comment
import com.example.sportscommunity.ReComment
import com.example.sportscommunity.databinding.ChatItemListBinding
import com.example.sportscommunity.databinding.ReChatItemListBinding

class ChatAdapter(
    private val commentList: MutableList<Comment>?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemClickListeners: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    inner class ViewHolder(val binding: ChatItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.chatText.setOnClickListener {
                itemClickListeners.onClick(it, adapterPosition)
            }
        }

        fun onBind(data: Comment) {
            binding.comment = data
        }
    }

    class ReViewHolder(val bindingTwo: ReChatItemListBinding) :
        RecyclerView.ViewHolder(bindingTwo.root) {
        fun onBind(data: Comment) {
            bindingTwo.reComment = data
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (commentList!![position].isReComment) {
            true -> 1
            false -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ChatItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val bindingRe =
            ReChatItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            1 -> ReViewHolder(bindingRe)
            else -> ViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = commentList?.size!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (commentList!![position].isReComment) {
            false -> (holder as ViewHolder).apply {
                holder.onBind(commentList[position])
                holder.binding.chatText.setOnClickListener {
                    itemClickListeners.onClick(it, position)
                }
            }
            true -> (holder as ReViewHolder).apply {
                holder.onBind(commentList[position])
                holder.bindingTwo.chatText.setOnClickListener {
                    itemClickListeners.onClick(it, position)
                }
            }
//        val holders = holder as ViewHolder
//        holders.binding.chatText.setOnClickListener {
//            itemClickListeners.onClick(it,position)
//        }
//        val holderss = holder as ReViewHolder
//        holderss.bindingTwo.chatText.setOnClickListener {
//            itemClickListeners.onClick(it,position)
//        }
        }
    }
    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListeners = itemClickListener
    }
}