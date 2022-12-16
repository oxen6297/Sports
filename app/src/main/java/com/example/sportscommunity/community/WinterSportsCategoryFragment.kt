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
import com.example.sportscommunity.Adapter.WaterSportsAdapter
import com.example.sportscommunity.Adapter.WinterSportsAdapter
import com.example.sportscommunity.databinding.WinterSportsCategoryTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WinterSportsCategoryFragment : Fragment() {

    private var mBinding: WinterSportsCategoryTabBinding? = null
    private val binding get() = mBinding!!
    private val contentList = mutableListOf<Content>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WinterSportsCategoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)

        getCommunityRetrofit()

        binding.run {
            winterSportsBoardRecycle.scrollToPosition(contentList.size-1)
        }

        mainActivity.hideBottomNavigationView(true)

        binding.write.setOnClickListener {
            mainActivity.changeFragment(0)
            mainActivity.setDataAtFragment(WriteContentFragment(),1 ,"write")
            writeFlag.put("write",5)
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
        val retrofitService = Retrofits.getWinterSportsService()
        val call: Call<WinterSportsTab> = retrofitService.getCommunity()
        val mainActivity = activity as MainActivity

        call.enqueue(object : Callback<WinterSportsTab> {
            override fun onResponse(call: Call<WinterSportsTab>, response: Response<WinterSportsTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.winterSportsBoardRecycle.apply {
                            this.adapter = WinterSportsAdapter(response.body()?.boardwrite5,mainActivity)
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

            override fun onFailure(call: Call<WinterSportsTab>, t: Throwable) {
                call.cancel()
            }
        })
    }
}