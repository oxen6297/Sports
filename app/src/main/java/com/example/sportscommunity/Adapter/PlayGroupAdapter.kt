package com.example.sportscommunity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.GroupPlay
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.PlayGroupItemListBinding

class PlayGroupAdapter(
    private val context: Context, private val playGroup: MutableList<GroupPlay>?
) : RecyclerView.Adapter<PlayGroupAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PlayGroupItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PlayGroupItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.run {

            val model = playGroup!![position]
            val url = model.groupImg

            Glide.with(context).load(url).error(R.drawable.picture).fitCenter()
                .into(groupProfileImg)

            groupName.text = model.groupTitle
            groupCategory.text = model.groupCategory
            groupArea.text = model.groupArea
            groupComment.text = model.groupCategory
            numberMember.text = model.groupMemberNumber
            groupTime.text = model.groupTime
        }
    }

    override fun getItemCount(): Int = playGroup!!.size
}