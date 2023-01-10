package com.example.sportscommunity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.CommunityItemListBinding

class FreeBoardAdapter(
    private var communityList: MutableList<Content>?, val mainActivity: MainActivity
) : RecyclerView.Adapter<FreeBoardAdapter.ViewHolder>(), Filterable {

    var filterCommunityList: MutableList<Content>? = communityList

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
        val item = communityList!![position]
        //제목, 프사, 닉네임, 시간, 내용
        holder.binding.communityLayout.setOnClickListener {
            titleHash.put("title", item.title.toString())
            descriptionHash.put("description", item.description.toString())
            userImageHash.put("image", item.profileimage.toString())
            nicknameHash.put("nickname", item.nickname.toString())
            writedateHash.put("writedate", holder.binding.writedate.text.toString())
            FreeBoardId.put("boardId", item.boardid.toString())
            categoryHash.put("categoryId", item.id.toString())

            mainActivity.changeFragment(19)
        }
    }

    override fun getItemCount(): Int = communityList?.size ?: 0

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().replace(" ","")
                communityList = if (charString.isEmpty()) {
                    filterCommunityList
                } else {
                    val filterList = mutableListOf<Content>()
                    if (filterCommunityList != null) {
                        for (item in filterCommunityList!!) {
                            if (item.title!!.replace(" ", "")
                                    .contains(charString) || item.nickname?.replace(" ", "")
                                    ?.contains(charString)!!
                            ) {
                                filterList.add(item)
                            }
                        }
                    }
                    filterList
                }
                val filterResults = FilterResults()
                filterResults.values = communityList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                communityList = results?.values as MutableList<Content>?
                notifyDataSetChanged()
            }
        }
    }
}