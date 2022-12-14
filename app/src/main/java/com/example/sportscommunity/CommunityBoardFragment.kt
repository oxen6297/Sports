package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.sportscommunity.adapter.ChatAdapter
import com.example.sportscommunity.databinding.CommunityBoardFragmentBinding
import com.example.sportscommunity.repository.Repository
import com.example.sportscommunity.sharedpreference.SharedPreferenceManager
import com.example.sportscommunity.viewmodel.ChatViewModel
import com.example.sportscommunity.viewmodel.LikeViewModel
import com.example.sportscommunity.viewmodelfactory.LikeViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

@AndroidEntryPoint
class CommunityBoardFragment : Fragment() {
    private val chatViewModel: ChatViewModel by viewModels()
    private val likeViewModel: LikeViewModel by viewModels { LikeViewModelFactory(Repository()) }
    lateinit var binding: CommunityBoardFragmentBinding
    private val boardId = FreeBoardId["boardId"].toString()
    private val titles = titleHash["title"].toString()
    private val descriptions = descriptionHash["description"].toString()
    private val images = userImageHash["image"].toString()
    private val nickname = nicknameHash["nickname"].toString()
    private val writedates = writedateHash["writedate"].toString()
    private val categoryId = categoryHash["categoryId"].toString()
    private var flag = 0
    private val commentList = mutableListOf<Comment>()
    private var chatAdpater = ChatAdapter()
    private var description = ""

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

    @SuppressLint("UseCompatLoadingForDrawables", "CommitPrefEdits", "ApplySharedPref",
        "NotifyDataSetChanged"
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chatRecycle.adapter = chatAdpater
        description = binding.chatEdit.text.toString()

        postLikeCount()
        getChat()
        getReChat()
        postChat()

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        val sharedPreferences =
            requireActivity().getSharedPreferences("FreeLike", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.run {

            flag = sharedPreferences.getInt(boardId, 0)

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

            //sp??? ??????????????? ??????, ???????????? ??????????????? ?????? ????????? ????????????????????? sp??? ??????,
            //????????????????????? sp ??? ???????????? if??? ?????? ?????????????????? ????????? 1?????? ??????, ????????? ????????????
            titleContent.text = titles
            writer.text = nickname
            contentText.text = descriptions
            timeText.text = writedates
            Glide.with(requireContext()).load(images).fitCenter().error(R.color.orange)
                .into(contentProfileImg)

            sendBtn.setOnClickListener {
                val chat = HashMap<String, Any>()
                val id = SharedPreferenceManager.getInt(requireContext(), "id", 0)

                val currentDate = LocalDateTime.now()
                val formatter = DateTimeFormatter.ISO_DATE
                val formatted = currentDate.format(formatter)
                val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
                val formattedTime = currentDate.format(formatterTwo)
                val writeTime = "$formatted $formattedTime"
                Log.d("chatchat",chatEdit.text.toString())

                chat["nickname"] = nickname
                chat["description"] = description
                chat["userid"] = id
                chat["categoryid"] = categoryId.toInt()
                chat["inherentid"] = boardId.toInt()
                chat["writedate"] = writeTime
                chat["profileimage"] = images
                chatViewModel.postChat(chat, "commentswrite")
                chatEdit.text = null
                softkeyboardHide()
            }

            chatAdpater.setItemClickListener(object : ChatAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    softkeyboard()
                    sendBtnTwo.visibility = View.VISIBLE
                    sendBtn.visibility = View.GONE
                    sendBtnTwo.setOnClickListener {
                        softkeyboardHide()
                        val commentsId = commentsId["commentsId"].toString().toInt()
                        postReChat(position, commentsId)
                        chatEdit.text = null
                        it.visibility = View.GONE
                        sendBtn.visibility = View.VISIBLE
                    }
                }
            })
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

    private fun unLike() {
        val unLike = HashMap<String, Any>()
        val id = SharedPreferenceManager.getInt(requireContext(), "id", 0)

        unLike["inherentid"] = boardId.toInt()
        unLike["categoryid"] = categoryId.toInt()
        unLike["userid"] = id

        val retrofitService = Retrofits.communityDeleteLikeNumber()
        val call: Call<String> = retrofitService.comDeleteLike(unLike)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
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
                    Log.d("unerror", e.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("unfailed", t.message.toString())
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun postReChat(position: Int, commentId: Int) {
        val chat = HashMap<String, Any>()

        val description = binding.chatEdit.text.toString()

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)
        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)
        val writeTime = "$formatted $formattedTime"
        val id = SharedPreferenceManager.getInt(requireContext(), "id", 0)

        chat["nickname"] = nickname
        chat["description"] = description
        chat["userid"] = id
        chat["categoryid"] = categoryId.toInt()
        chat["inherentid"] = boardId.toInt()
        chat["writedate"] = writeTime
        chat["profileimage"] = images
        chat["inherentcommentsid"] = commentId

        chatViewModel.postReChatResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val body = it.body().toString()

                val chatNumber = JSONObject(body).getJSONObject("replycount")
                val chatCounts = chatNumber.getString("commentcount").toString()
                val commentsid = chatNumber.getString("replyid").toString()

                commentList.add(
                    position,
                    Comment(
                        nickname,
                        description,
                        writeTime,
                        boardId.toInt(),
                        categoryId.toInt(),
                        id,
                        images,
                        1,
                        commentsid.toInt(),
                        0,
                        null
                    )
                )

                binding.chatNum.text = chatCounts
                chatAdpater.notifyItemInserted(position)
            }
        }

        chatViewModel.postReChat(chat, "replywrite")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun postChat() {
        val id = SharedPreferenceManager.getInt(requireContext(), "id", 0)

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)
        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)
        val writeTime = "$formatted $formattedTime"

        chatViewModel.postChatResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val chatNumber =
                    JSONObject(it.body().toString()).getJSONObject("commentcount")
                val chatCounts = chatNumber.getInt("commentcount").toString()
                val commentsid = chatNumber.getInt("commentsid").toString()

                commentList.add(
                    Comment(
                        nickname,
                        description,
                        writeTime,
                        boardId.toInt(),
                        categoryId.toInt(),
                        id,
                        images,
                        2,
                        commentsid.toInt(),
                        0,
                        null
                    )
                )

                if (chatCounts.isEmpty()) {
                    binding.chatNum.text = "0"
                } else {
                    binding.chatNum.text = chatCounts
                }
                chatAdpater.setData(commentList,commentList.size-1)
            }
        }
    }

    private fun postLike() {
        val id = SharedPreferenceManager.getInt(requireContext(), "id", 0)
        val like = HashMap<String, Any>()

        like["inherentid"] = boardId.toInt()
        like["categoryid"] = categoryId.toInt()
        like["userid"] = id

        likeViewModel.postComLike.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val body = it.body().toString()

                val likeNumber = JSONObject(body).getJSONObject("likedcount")
                val likeCounts = likeNumber.getString("likedcount").toString()
                if (likeCounts.isEmpty()) {
                    binding.likeNum.text = "0"
                } else {
                    binding.likeNum.text = likeCounts
                }
            }
        }
        likeViewModel.postComLike(like)
    }

    private fun postLikeCount() {
        val likeCount = HashMap<String, Any>()

        likeCount["inherentid"] = boardId.toInt()
        likeCount["categoryid"] = categoryId.toInt()

        likeViewModel.getComLike.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val body = it.body().toString()

                val likeNumber = JSONObject(body).getJSONObject("likedcount")
                val likeCounts = likeNumber.getString("likedcount")
                binding.likeNum.text = likeCounts.toString()

                Log.d("likedCount", likeCounts)
            }
        }
        likeViewModel.getComLike(likeCount)
    }

    private fun softkeyboardHide() {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.chatEdit.windowToken, 0)
    }

    private fun softkeyboard() {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.chatEdit, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getChat() {

        val chat = HashMap<String, Any>()
        chat["inherentid"] = boardId.toInt()
        chat["categoryid"] = categoryId.toInt()

        chatViewModel.getChatResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val res = it.body()
                val chatInfo = JSONObject(res.toString())
                val chatArray = chatInfo.optJSONArray("comment")
                var i = 0
                if (chatArray != null) {
                    while (i < chatArray.length()) {
                        val jsonObject = chatArray.getJSONObject(i)
                        val nickname = jsonObject.getString("nickname")
                        val description = jsonObject.getString("description")
                        val writedate = jsonObject.getString("writedate")
                        val profileimage = jsonObject.getString("profileimage")
                        val isrecomment = jsonObject.getString("isrecomment").toInt()
                        val inherentid = jsonObject.getString("inherentid").toInt()
                        val categoryid = jsonObject.getString("categoryid").toInt()
                        val userid = jsonObject.getString("userid").toInt()
                        val inherentCommentsid = jsonObject.getString("commentsid").toInt()
                        val likedCount = jsonObject.getString("likedcount").toInt()
                        val chatCount = jsonObject.getString("commentcount").toString()

                        commentList.add(
                            Comment(
                                nickname,
                                description,
                                writedate,
                                inherentid,
                                categoryid,
                                userid,
                                profileimage,
                                isrecomment,
                                inherentCommentsid,
                                likedCount,
                                null
                            )
                        )
                        i++
                        if (chatCount == null){
                            binding.chatNum.text = "0"
                        } else {
                            binding.chatNum.text = chatCount
                        }
                        commentList.sortBy { it.commentsid }
                        chatAdpater.getSetData(commentList)
                    }
                }
            }
        }
        chatViewModel.getChat(chat, "commentsinfo")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getReChat() {
        val chat = HashMap<String, Any>()
        chat["inherentid"] = boardId.toInt()
        chat["categoryid"] = categoryId.toInt()

        chatViewModel.getReChatResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val res = it.body()
                val chatInfo = JSONObject(res.toString())
                val chatArray = chatInfo.optJSONArray("reply")
                var i = 0
                if (chatArray != null) {
                    while (i < chatArray.length()) {
                        val jsonObject = chatArray.getJSONObject(i)
                        val nickname = jsonObject.getString("nickname")
                        val description = jsonObject.getString("description")
                        val writedate = jsonObject.getString("writedate")
                        val profileimage = jsonObject.getString("profileimage")
                        val isrecomment = jsonObject.getString("isrecomment").toInt()
                        val inherentid = jsonObject.getString("inherentid").toInt()
                        val categoryid = jsonObject.getString("categoryid").toInt()
                        val userid = jsonObject.getString("userid").toInt()
                        val inherentCommentsid =
                            jsonObject.getString("inherentcommentsid").toInt()
                        val likedCount = jsonObject.getString("likedcount").toInt()
                        val replyId = jsonObject.getString("replyid").toInt()

                        commentList.add(
                            Comment(
                                nickname,
                                description,
                                writedate,
                                inherentid,
                                categoryid,
                                userid,
                                profileimage,
                                isrecomment,
                                inherentCommentsid,
                                likedCount,
                                replyId
                            )
                        )
                        i++
                        commentList.sortBy { it.replyid }
                        commentList.reverse()
                        chatAdpater.getSetData(commentList)
                    }
                }
            }
        }
        chatViewModel.getReChat(chat, "replyinfo")
    }
}