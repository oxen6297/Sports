package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.Comment
import com.example.sportscommunity.commentsId
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
        fun onBind(data: Comment) {
            binding.comment = data
        }
    }

    inner class ReViewHolder(val bindingTwo: ReChatItemListBinding) :
        RecyclerView.ViewHolder(bindingTwo.root) {
        fun onBind(data: Comment) {
            bindingTwo.reComment = data
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (commentList!![position].isrecomment) {
            1 -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ChatItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val bindingRe =
            ReChatItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            1 -> ReViewHolder(bindingRe)
            2 -> ViewHolder(binding)
            else -> ViewHolder(binding)
        }
    }

    override fun getItemCount(): Int{
        return commentList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (commentList!![position].isrecomment) {
            2 -> (holder as ViewHolder).apply {
                holder.onBind(commentList[position])
                holder.binding.chatText.setOnClickListener {
                    itemClickListeners.onClick(it, position)
                    commentsId.put("commentsId",binding.inherentid.text.toString())

                }
            }
            1 -> (holder as ReViewHolder).apply {
                holder.onBind(commentList[position])
                holder.bindingTwo.chatText.setOnClickListener {
                    itemClickListeners.onClick(it, position)
                    commentsId.put("commentsId",bindingTwo.inherentid.text.toString())
                }
            }
        }
    }
    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListeners = itemClickListener
    }
}
