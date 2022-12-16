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
import com.example.sportscommunity.Adapter.ESportsAdapter
import com.example.sportscommunity.Adapter.FaqBoardAdapter
import com.example.sportscommunity.databinding.FaqCategoryTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaQCategoryFragment:Fragment() {

    private var mBinding: FaqCategoryTabBinding? = null
    private val binding get() = mBinding!!
    private val contentList = mutableListOf<Content>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FaqCategoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)

        binding.run {
            faqBoardRecycle.scrollToPosition(contentList.size-1)
        }

        getCommunityRetrofit()

        mainActivity.hideBottomNavigationView(true)

        binding.write.setOnClickListener {
            mainActivity.changeFragment(0)
            mainActivity.setDataAtFragment(WriteContentFragment(),1 ,"write")
            writeFlag.put("write",10)
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

    private fun getCommunityRetrofit() {
        val retrofitService = Retrofits.getFaqBoardService()
        val call: Call<FaqBoardTab> = retrofitService.getCommunity()
        val mainActivity = activity as MainActivity

        call.enqueue(object : Callback<FaqBoardTab> {
            override fun onResponse(call: Call<FaqBoardTab>, response: Response<FaqBoardTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.faqBoardRecycle.apply {
                            this.adapter = FaqBoardAdapter(response.body()?.boardwrite10,mainActivity)
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

            override fun onFailure(call: Call<FaqBoardTab>, t: Throwable) {
                call.cancel()
            }
        })
    }
}