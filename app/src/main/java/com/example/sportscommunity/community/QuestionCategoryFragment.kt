package com.example.sportscommunity.community

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.*
import com.example.sportscommunity.Adapter.LeisureSportsAdpater
import com.example.sportscommunity.Adapter.QuestionBoardAdapter
import com.example.sportscommunity.databinding.QuestionCategoryTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionCategoryFragment: Fragment() {

    private var mBinding: QuestionCategoryTabBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = QuestionCategoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)

        getCommunityRetrofit()

        mainActivity.hideBottomNavigationView(true)

        binding.write.setOnClickListener {
            mainActivity.changeFragment(0)
            mainActivity.setDataAtFragment(WriteContentFragment(),1 ,"write")
            writeFlag.put("write",9)
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

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "질문 게시판"
    }

    private fun getCommunityRetrofit() {
        val retrofitService = Retrofits.getQuestionBoardService()
        val call: Call<QuestionBoardTab> = retrofitService.getCommunity()

        call.enqueue(object : Callback<QuestionBoardTab> {
            override fun onResponse(call: Call<QuestionBoardTab>, response: Response<QuestionBoardTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.questionBoardRecycle.apply {
                            this.adapter = QuestionBoardAdapter(response.body()?.boardwrite9)
                            this.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                true
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<QuestionBoardTab>, t: Throwable) {
                call.cancel()
            }
        })
    }
}