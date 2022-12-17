package com.example.sportscommunity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.CommunityItemListBinding

class WaterSportsAdapter(
    private val communityList: MutableList<Content>?, val mainActivity: MainActivity
) : RecyclerView.Adapter<WaterSportsAdapter.ViewHolder>() {

    class ViewHolder(val binding: CommunityItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Content) {
            binding.com = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CommunityItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(communityList!![position])
        val item = communityList[position]
        //제목, 프사, 닉네임, 시간, 내용
        holder.binding.communityLayout.setOnClickListener {
            titleHash.put("title", item.title.toString())
            descriptionHash.put("description", item.description.toString())
            userImageHash.put("image", item.profileimage.toString())
            nicknameHash.put("nickname", item.nickname.toString())
            writedateHash.put("writedate", holder.binding.writedate.text.toString())
            FreeBoardId.put("boardId",item.boardid.toString())
            categoryHash.put("categoryId",item.categoryid.toString())
            WaterBoardId.put("boardId",item.boardid.toString())

            mainActivity.changeFragment(19)
        }
    }

    override fun getItemCount(): Int = communityList!!.size
}