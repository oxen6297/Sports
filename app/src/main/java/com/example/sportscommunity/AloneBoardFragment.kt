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
import com.example.sportscommunity.databinding.AloneBoardFragmentBinding
import org.apache.log4j.chainsaw.Main
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AloneBoardFragment : Fragment() {

    lateinit var binding: AloneBoardFragmentBinding
    lateinit var callback: OnBackPressedCallback
    private val titles = titleHash["title"].toString()
    private val categorys = categoryHash["category"].toString()
    private val locals = localHash["local"].toString()
    private val descriptions = descriptionHash["description"].toString()
    private val genders = genderHash["gender"].toString()
    private val minages = minageHash["minage"].toString()
    private val maxages = maxageHash["maxage"].toString()
    private val images = userImageHash["image"].toString()
    private val nicknames = nicknameHash["nickname"].toString()
    private val writedates = writedateHash["writedate"].toString()
    private val boardId = AloneBoardId["alone"].toString()
    private val categoryId = AloneCategoryHash["categoryid"].toString().toInt()
    private var flag = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.alone_board_fragment, container, false)
        return binding.root
    }

    @SuppressLint(
        "SetTextI18n", "CommitPrefEdits", "UseCompatLoadingForDrawables",
        "ApplySharedPref"
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postLikeCount()

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        val sharedPreferences =
            requireActivity().getSharedPreferences("AloneLike", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.run {

            flag = sharedPreferences.getInt("Alone$boardId", 0)
            //제목, 닉네임, 지역, 시간, 카테고리, 제한(성별, 나이), 내용, 프사

            playTitle.text = titles
            playCategory.text = categorys
            userArea.text = locals
            userComment.text = descriptions
            userSelectManWoman.text = genders
            if (minages == "상관없음") {
                age.text = "상관없음"
            } else {
                age.text = minages + "세" + " ~ " + maxages + "세"
            }
            Glide.with(requireContext()).load(images).fitCenter().error(R.color.orange)
                .into(userProfileImg)
            userId.text = nicknames
            time.text = writedates

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

            heartLayout.setOnClickListener {
                if (flag == 0) {
                    likeImg.setImageDrawable(
                        requireContext().resources.getDrawable(
                            R.drawable.red_heart,
                            null
                        )
                    )
                    flag = 1
                    editor.putInt("Alone$boardId", flag)
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
                    editor.putInt("Alone$boardId", flag)
                    editor.commit()
                    unLike()
                }
            }
        }
    }

    private fun unLike() {
        val unLike = HashMap<String, Any>()

        unLike["inherentid"] = boardId.toInt()
        unLike["categoryid"] = categoryId
        unLike["userid"] = 3

        val retrofitService = Retrofits.aloneDeleteLikeNumber()
        val call: Call<Like> = retrofitService.aloneDeleteLike(unLike)

        call.enqueue(object : Callback<Like> {
            override fun onResponse(call: Call<Like>, response: Response<Like>) {
                try {
                    if (response.isSuccessful) {
                        val num = binding.likeNum.text.toString().toInt() - 1
                        if (num < 0) {
                            binding.likeNum.text = "0"
                        } else if (num >= 0) {
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
        like["categoryid"] = categoryId
        like["userid"] = 3

        val retrofitService = Retrofits.postAloneLikeNumber()
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
        likeCount["categoryid"] = categoryId

        val retrofitService = Retrofits.postAloneLikeCount()
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

        val mainActivity = activity as MainActivity

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapFragment())
                    .commit()
                mainActivity.itemSelected()
                mainActivity.setDataAtFragment(SportsMapGroupFragment(), 1, "alone")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}