package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.PlayGroupItemListBinding
import java.text.SimpleDateFormat
import java.util.*

class PlayGroupAdapter(
    private val context: Context,
    private val playGroup: MutableList<GroupPlay>?,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<PlayGroupAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PlayGroupItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: GroupPlay) {
            binding.groups = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PlayGroupItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(playGroup!![position])

        val item = playGroup[position]

        holder.binding.groupLayout.setOnClickListener {

            titleHash.put("title", item.title.toString())
            categoryHash.put("category", holder.binding.groupCategory.text.toString())
            onceHash.put("once", item.once.toString())
            localHash.put("local", item.local.toString())
            lineHash.put("line", item.line.toString())
            descriptionHash.put("description", item.description.toString())
            nownumHash.put("nownum", item.peoplenownum.toString())
            peoplenumHash.put("peoplenum", item.peoplenum.toString())
            genderHash.put("gender", item.gender.toString())
            minageHash.put("minage", item.minage.toString())
            maxageHash.put("maxage", item.maxage.toString())
            titleImageHash.put("image", item.titleimage.toString())

            mainActivity.changeFragment(16)
        }

        when (item.id) {
            1 -> {
                holder.binding.groupCategory.text = "구기종목"
            }
            2 -> {
                holder.binding.groupCategory.text = "레져"
            }
            3 -> {
                holder.binding.groupCategory.text = "해양 스포츠"
            }
            4 -> {
                holder.binding.groupCategory.text = "생활 스포츠"
            }
            5 -> {
                holder.binding.groupCategory.text = "동계 스포츠"
            }
            6 -> {
                holder.binding.groupCategory.text = "E-스포츠"
            }
        }

//        holder.itemView.setOnClickListener {
//            activity.changeFragment(16)
////            activity.setDataAtFragmentThree(GroupBoardFragment(),item,"title")
//
//        }

        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        var date: Date? = null

        date = formatter.parse(item.writedate.toString())
        val curTime = System.currentTimeMillis().toDouble()
        val regTime = date?.time?.toDouble()
        val diffTime = (curTime - regTime!!) / 1000

        val a = diffTime / 60
        val b = diffTime / 60
        val c = diffTime / 24

        if (diffTime < 60) {
            holder.binding.groupTime.text = "방금전"
        } else if (a < 60) {
            holder.binding.groupTime.text = diffTime.toString() + "분 전"
        } else if (b < 24) {
            holder.binding.groupTime.text = diffTime.toString() + "시간 전"
        } else if (c < 7) {
            holder.binding.groupTime.text = diffTime.toString() + "일 전"
        }

        holder.binding.numberMember.text =
            item.peoplenownum.toString() + "/" + item.peoplenum.toString()

        Glide.with(context).load(item.titleimage).centerCrop()
            .into(holder.binding.groupProfileImg)
    }


    override fun getItemCount(): Int = playGroup!!.size
}