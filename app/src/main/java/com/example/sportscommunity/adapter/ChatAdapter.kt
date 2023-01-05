package com.example.sportscommunity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.Comment
import com.example.sportscommunity.commentsId
import com.example.sportscommunity.databinding.ChatItemListBinding
import com.example.sportscommunity.databinding.ReChatItemListBinding

class ChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemClickListeners: OnItemClickListener
    var commentList:MutableList<Comment>? = mutableListOf()

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(commentList: MutableList<Comment>,position: Int){
        this.commentList = commentList
        notifyItemInserted(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getSetData(commentList: MutableList<Comment>){
        this.commentList = commentList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ChatItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
            else -> ViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = commentList?.size ?: 0


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (commentList?.get(position)?.isrecomment) {

            1 -> (holder as ReViewHolder).apply {
                holder.onBind(commentList!![position])
                holder.bindingTwo.chatText.setOnClickListener {
                    itemClickListeners.onClick(it, position)
                    commentsId["commentsId"] = bindingTwo.inherentid.text.toString()
                }
            }
            2 -> (holder as ViewHolder).apply {
                holder.onBind(commentList!![position])
                holder.binding.chatText.setOnClickListener {
                    itemClickListeners.onClick(it, position)
                    commentsId["commentsId"] = binding.inherentid.text.toString()
                }
            }
        }
    }
    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListeners = itemClickListener
    }
}
