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
import com.example.sportscommunity.*
import com.example.sportscommunity.Adapter.ESportsAdapter
import com.example.sportscommunity.databinding.ESportsFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ESportsCategoryFragment : Fragment() {

    private var mBinding: ESportsFragmentBinding? = null
    private val binding get() = mBinding!!
    private val esportsList = mutableListOf<Content>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = ESportsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)

        binding.run {
            gameBoardRecycle.scrollToPosition(esportsList.size-1)
        }
        getCommunityRetrofit()

        mainActivity.hideBottomNavigationView(true)

        binding.write.setOnClickListener {
            mainActivity.changeFragment(0)
            mainActivity.setDataAtFragment(WriteContentFragment(), 1, "write")
            writeFlag.put("write", 6)
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
        val retrofitService = Retrofits.getESportsService()
        val call: Call<ESportsTab> = retrofitService.getCommunity()
        val mainActivity = activity as MainActivity

        call.enqueue(object : Callback<ESportsTab> {
            override fun onResponse(call: Call<ESportsTab>, response: Response<ESportsTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.gameBoardRecycle.apply {
                            this.adapter =
                                ESportsAdapter(response.body()?.boardwrite6, mainActivity)
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

            override fun onFailure(call: Call<ESportsTab>, t: Throwable) {
                call.cancel()
            }
        })
    }
}