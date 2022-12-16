package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sportscommunity.databinding.CommunityBoardFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityBoardFragment : Fragment() {

    lateinit var binding: CommunityBoardFragmentBinding
    private val boardId = FreeBoardId["boardId"].toString()
    private val titles = titleHash["title"].toString()
    private val descriptions = descriptionHash["description"].toString()
    private val images = userImageHash["image"].toString()
    private val nickname = nicknameHash["nickname"].toString()
    private val writedates = writedateHash["writedate"].toString()
    private val categoryId = categoryHash["categoryId"].toString()

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.community_board_fragment, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "CommitPrefEdits", "ApplySharedPref")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        val sharedPreferences =
            requireActivity().getSharedPreferences("FreeLike", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.run {

            var flag = sharedPreferences.getInt(boardId, 0)

            heartLayout.setOnClickListener {
                if (flag == 0) {
                    likeImg.setImageDrawable(
                        requireContext().resources.getDrawable(
                            R.drawable.red_heart,
                            null
                        )
                    )
                    flag = 1
                    editor.putInt(boardId, flag)
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
                    editor.putInt(boardId, flag)
                    editor.commit()
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

            //sp에 보드아이디 키값, 플래그를 밸류값으로 하고 클릭시 플래그올라가고 sp에 저장,
            //온크리에이트시 sp 값 가져와서 if로 현재 보드아이디와 일치시 1이면 빨강, 아니면 그냥하트
            titleContent.text = titles
            writer.text = nickname
            contentText.text = descriptions
            timeText.text = writedates
            Glide.with(requireContext()).load(images).fitCenter().error(R.color.orange)
                .into(contentProfileImg)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun postLike() {
        val like = HashMap<String, Any>()

        like["inherentid"] = boardId
        like["nickname"] = nickname
        like["categoryid"] = categoryId
        like["userid"] = 3

        val retrofitService = Retrofits.postLikeNumber()
        val call: Call<Like> = retrofitService.postLike(like)

        call.enqueue(object : Callback<Like> {
            @SuppressLint("CommitPrefEdits")
            override fun onResponse(call: Call<Like>, response: Response<Like>) {
                try {
                    if (response.isSuccessful) {
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
}