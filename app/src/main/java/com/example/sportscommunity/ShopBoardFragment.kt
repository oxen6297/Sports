package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sportscommunity.databinding.ShopBoardFragmentBinding
import org.apache.log4j.chainsaw.Main

class ShopBoardFragment : Fragment() {

    lateinit var binding: ShopBoardFragmentBinding
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.shop_board_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            //사진5개, 제목, 카테고리, 지역, 가격, 시간, 프사, 닉네임, 내용
            val titles = titleHash["title"].toString()
            val categorys = categoryHash["category"].toString().toInt()
            val locals = localHash["local"].toString()
            val descriptions = descriptionHash["description"].toString()
            val nicknames = nicknameHash["nickname"].toString()
            val writedates = writedateHash["writedate"].toString()
            val prices = priceHash["price"].toString()
            val userImage = userImageHash["userimage"].toString()
            val imageOne = shopImageHash["imageOne"].toString()
            val imageTwo = shopImageHashTwo["imageTwo"].toString()
            val imageThree = shopImageHashThree["imageThree"].toString()
            val imageFour = shopImageHashFour["imageFour"].toString()
            val imageFive = shopImageHashFive["imageFive"].toString()

            shopTitle.text = titles
            local.text = locals
            contentText.text = descriptions
            Glide.with(requireContext()).load(userImage).fitCenter().error(R.color.orange)
                .into(profileImg)
            writer.text = nicknames
            timeText.text = writedates
            price.text = prices

            when (categorys) {
                1 -> {
                    category.text = "구기종목"
                }
                2 -> {
                    category.text = "레져"
                }
                3 -> {
                    category.text = "해양 스포츠"
                }
                4 -> {
                    category.text = "생활 스포츠"
                }
                5 -> {
                    category.text = "동계 스포츠"
                }
                6 -> {
                    category.text = "E-스포츠"
                }
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsShopFragment())
                    .commit()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}