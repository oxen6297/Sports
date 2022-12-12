package com.example.sportscommunity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportscommunity.GroupPlay
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.PlayGroupItemListBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.SimpleFormatter
import kotlin.collections.ArrayList

class PlayGroupAdapter(
    private val context: Context, private val playGroup: MutableList<GroupPlay>?
) : RecyclerView.Adapter<PlayGroupAdapter.ViewHolder>(), Filterable {

    private lateinit var itemClickListener: PlayGroupAdapter.OnItemClickListener
    private var files = playGroup
    private var unfiles = playGroup

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

        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        var date: Date? = null

        date = formatter.parse(item.writedate)
        val curTime = System.currentTimeMillis()
        val regTime = date?.time
        val diffTime = (curTime - regTime!!) / 1000

        val a = diffTime/60
        val b = diffTime/60
        val c = diffTime/24

        if (diffTime < 60) {
            holder.binding.groupTime.text = "방금전"
        } else if (a<60){
            holder.binding.groupTime.text = diffTime.toString() + "분 전"
        } else if(b<24){
            holder.binding.groupTime.text = diffTime.toString() + "시간 전"
        } else if(c<7){
            holder.binding.groupTime.text = diffTime.toString() + "일 전"
        }

        holder.binding.groupLayout.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.binding.numberMember.text =
            item.peoplenownum.toString() + "/" + item.peoplenum.toString()

        Glide.with(context).load(item.titleimage).centerCrop()
            .into(holder.binding.groupProfileImg)

    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                files = if (charString.isEmpty()) {
                    unfiles
                } else {
                    val filterList = ArrayList<GroupPlay>()
                    for (item in unfiles!!) {
                        if (item.title == charString) filterList.add(item)
                    }
                    filterList
                }
                val filterResult = FilterResults()
                filterResult.values = files
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                files = results?.values as ArrayList<GroupPlay>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = playGroup!!.size
}