package com.example.sportscommunity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.PlayWith
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.PlayWithItemListBinding

class PlayWithAdapter(
    private val context: Context, private val playWith: MutableList<PlayWith>?
) : RecyclerView.Adapter<PlayWithAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PlayWithItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayWithAdapter.ViewHolder {
        val binding =
            PlayWithItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayWithAdapter.ViewHolder, position: Int) {

        holder.binding.run {
            val model = playWith!![position]
            val url = model.userImg

            Glide.with(context).load(url).error(R.drawable.picture).fitCenter()
                .into(userProfileImg)

            playTitle.text = model.playTitle
            userId.text = model.userId
            userArea.text = model.userArea
            time.text = model.time
            goOnPlay.text = model.goOn
            playCategory.text = model.playCategory
            userSelectManWoman.text = model.manWoman
            userComment.text = model.playComment
        }
    }

    override fun getItemCount(): Int = playWith!!.size
}