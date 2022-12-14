package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sportscommunity.databinding.ShopBoardFragmentBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopBoardFragment : Fragment() {

    lateinit var binding: ShopBoardFragmentBinding
    lateinit var callback: OnBackPressedCallback
    private val boardId = ShopBoardId["shop"].toString()
    private val titles = titleHash["title"].toString()
    private val categorys = categoryHash["category"].toString().toInt()
    private val locals = localHash["local"].toString()
    private val descriptions = descriptionHash["description"].toString()
    private val nicknames = nicknameHash["nickname"].toString()
    private val writedates = writedateHash["writedate"].toString()
    private val prices = priceHash["price"].toString()
    private val userImage = userImageHash["userimage"].toString()
    private val imageOne = shopImageHash["imageOne"].toString()
    private val imageTwo = shopImageHashTwo["imageTwo"].toString()
    private val imageThree = shopImageHashThree["imageThree"].toString()
    private val imageFour = shopImageHashFour["imageFour"].toString()
    private val imageFive = shopImageHashFive["imageFive"].toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.shop_board_fragment, container, false)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits", "UseCompatLoadingForDrawables", "ApplySharedPref")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postLikeCount()

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        val sharedPreferences =
            requireActivity().getSharedPreferences("ShopLike", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.run {

            var flag = sharedPreferences.getInt("Shop$boardId", 0)

            heartLayout.setOnClickListener {
                if (flag == 0) {
                    likeImg.setImageDrawable(
                        requireContext().resources.getDrawable(
                            R.drawable.red_heart,
                            null
                        )
                    )
                    flag = 1
                    editor.putInt("Shop$boardId", flag)
                    editor.commit()
                    postLike()
                } else if (flag == 1) {
                    likeImg.setImageDrawable(
                        requireContext().resources.getDrawable(
                            R.drawable.heart,
                            null
                        )
                    )
                    flag = 0
                    editor.putInt("Shop$boardId", flag)
                    editor.commit()
                    unLike()
                }
            }

            if (flag == 1) {
                likeImg.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.red_heart,
                        null
                    )
                )
            } else if (flag == 0) {
                likeImg.setImageDrawable(
                    requireContext().resources.getDrawable(
                        R.drawable.heart,
                        null
                    )
                )
            }
            //??????5???, ??????, ????????????, ??????, ??????, ??????, ??????, ?????????, ??????
            shopTitle.text = titles
            local.text = locals
            contentText.text = descriptions
            Glide.with(requireContext()).load(userImage).fitCenter().error(R.color.orange)
                .into(profileImg)
            writer.text = nicknames
            timeText.text = writedates
            price.text = prices

//            when (categorys) {
//                1 -> {
//                    category.text = "????????????"
//                }
//                2 -> {
//                    category.text = "??????"
//                }
//                3 -> {
//                    category.text = "?????? ?????????"
//                }
//                4 -> {
//                    category.text = "?????? ?????????"
//                }
//                5 -> {
//                    category.text = "?????? ?????????"
//                }
//                6 -> {
//                    category.text = "E-?????????"
//                }
//            }
        }
    }

    private fun unLike(){
        val unLike = HashMap<String,Any>()

        unLike["inherentid"] = boardId.toInt()
        unLike["categoryid"] = categorys
        unLike["userid"] = 3

        val retrofitService = Retrofits.shopDeleteLikeNumber()
        val call: Call<Like> = retrofitService.shopDeleteLike(unLike)

        call.enqueue(object : Callback<Like> {
            override fun onResponse(call: Call<Like>, response: Response<Like>) {
                try {
                    if (response.isSuccessful) {
                        val num = binding.likeNum.text.toString().toInt() - 1
                        if (num < 0){
                            binding.likeNum.text = "0"
                        } else if (num >= 0){
                            binding.likeNum.text = num.toString()
                        }
                        Log.d("success", "success")
                    }
                } catch (e: Exception) {
                    Log.d("error", e.toString())
                }
            }

            override fun onFailure(call: Call<Like>, t: Throwable) {
                Log.d("failed", t.message.toString())
            }
        })
    }

    private fun postLike() {
        val like = HashMap<String, Any>()

        like["inherentid"] = boardId.toInt()
        like["categoryid"] = categorys
        like["userid"] = 3

        val retrofitService = Retrofits.postShopLikeNumber()
        val call: Call<String> = retrofitService.postLike(like)

        call.enqueue(object : Callback<String> {
            @SuppressLint("CommitPrefEdits")
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {
                        val body = response.body().toString()

                        val likeNumber = JSONObject(body).getJSONObject("likedcount")
                        val likeCounts = likeNumber.getString("likedcount").toString()
                        if (likeCounts.isEmpty()) {
                            binding.likeNum.text = "0"
                        } else {
                            binding.likeNum.text = likeCounts
                        }

                    }
                } catch (e: Exception) {
                    Log.d("error", e.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("failed", t.message.toString())
            }
        })
    }

    private fun postLikeCount() {
        val likeCount = HashMap<String, Any>()

        likeCount["inherentid"] = boardId.toInt()
        likeCount["categoryid"] = categorys

        val retrofitService = Retrofits.postShopLikeCount()
        val call: Call<String> = retrofitService.postLikeCount(likeCount)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    if (response.isSuccessful) {

                        val body = response.body().toString()

                        val likeNumber = JSONObject(body).getJSONObject("likedcount")
                        val likeCounts = likeNumber.getString("likedcount")
                        binding.likeNum.text = likeCounts.toString()

                        Log.d("likedCount", likeCounts)
                    }
                } catch (e: Exception) {
                    Log.d("errordd", e.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("faileddd", t.message.toString())
            }
        })
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