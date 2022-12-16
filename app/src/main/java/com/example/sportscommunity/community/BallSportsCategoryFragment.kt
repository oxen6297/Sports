package com.example.sportscommunity.community

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportscommunity.*
import com.example.sportscommunity.Adapter.BallSportsAdapter
import com.example.sportscommunity.databinding.BallCategoryTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BallSportsCategoryFragment : Fragment() {

    private var mBinding: BallCategoryTabBinding? = null
    private val binding get() = mBinding!!
    private val ballList = mutableListOf<Content>()

    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BallCategoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)

        getCommunityRetrofit()

        mainActivity.hideBottomNavigationView(true)

        binding.run {
            ballBoardRecycle.scrollToPosition(ballList.size-1)
        }

        binding.write.setOnClickListener {
            mainActivity.changeFragment(0)
            mainActivity.setDataAtFragment(WriteContentFragment(), 1, "write")
            writeFlag.put("write", 1)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun getCommunityRetrofit() {
        val retrofitService = Retrofits.getBallSportsService()
        val call: Call<BallSportsTab> = retrofitService.getCommunity()
        val mainActivity = activity as MainActivity

        call.enqueue(object : Callback<BallSportsTab> {
            override fun onResponse(call: Call<BallSportsTab>, response: Response<BallSportsTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.ballBoardRecycle.apply {
                            this.adapter = BallSportsAdapter(response.body()?.boardwrite1,mainActivity)
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

            override fun onFailure(call: Call<BallSportsTab>, t: Throwable) {
                call.cancel()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}