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
import com.example.sportscommunity.Adapter.FreeBoardAdapter
import com.example.sportscommunity.Adapter.LeisureSportsAdpater
import com.example.sportscommunity.databinding.LeisureCategoryTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeisureCategoryFragment:Fragment() {

    private var mBinding: LeisureCategoryTabBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LeisureCategoryTabBinding.inflate(inflater, container, false)
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
            writeFlag.put("write",2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val mainActivity = (activity as MainActivity)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "레져"
    }

    private fun getCommunityRetrofit() {
        val retrofitService = Retrofits.getLeisureSportsService()
        val call: Call<LeisureTab> = retrofitService.getCommunity()

        call.enqueue(object : Callback<LeisureTab> {
            override fun onResponse(call: Call<LeisureTab>, response: Response<LeisureTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.leisureBoardRecycle.apply {
                            this.adapter = LeisureSportsAdpater(response.body()?.boardwrite2)
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

            override fun onFailure(call: Call<LeisureTab>, t: Throwable) {
                call.cancel()
            }
        })
    }
}