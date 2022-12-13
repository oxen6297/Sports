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
import com.example.sportscommunity.Adapter.FaqBoardAdapter
import com.example.sportscommunity.Adapter.FreeBoardAdapter
import com.example.sportscommunity.databinding.FreeCategoryTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreeCategoryFragment:Fragment() {

    private var mBinding: FreeCategoryTabBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FreeCategoryTabBinding.inflate(inflater, container, false)
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
            writeFlag.put("write",7)
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

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "자유 게시판"
    }

    private fun getCommunityRetrofit() {
        val retrofitService = Retrofits.getFreeBoardService()
        val call: Call<FreeBoardTab> = retrofitService.getCommunity()

        call.enqueue(object : Callback<FreeBoardTab> {
            override fun onResponse(call: Call<FreeBoardTab>, response: Response<FreeBoardTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.freeBoardRecycle.apply {
                            this.adapter = FreeBoardAdapter(response.body()?.boardwrite7)
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

            override fun onFailure(call: Call<FreeBoardTab>, t: Throwable) {
                call.cancel()
            }
        })
    }
}