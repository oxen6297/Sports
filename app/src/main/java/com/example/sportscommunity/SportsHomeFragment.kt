package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsHomeFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsHomeFragment : Fragment() {

    lateinit var binding: SportsHomeFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.sports_home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.onclick = this

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigationView(false)


        CoroutineScope(Dispatchers.IO).launch {
            callNews()
        }



    }



    private fun callNews() {
        val retrofitService = Retrofits.getNewsService()
        val call: Call<NewsList> = retrofitService.getNewsList()

        call.enqueue(object : Callback<NewsList> {
            override fun onResponse(
                call: Call<NewsList>,
                response: Response<NewsList>
            ) {
                try {
                    if (response.isSuccessful) {

                        binding.newsRecycle.apply {
                            this.adapter = ListSourceAdapter(
                                requireContext(),
                                response.body()?.articles
                            )
                            this.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            Log.d("success", "loadSuccess")
                            Log.d("loadNews", response.body()?.articles.toString())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                Log.d("failed", "failed")
            }
        })
    }

    fun onClick(v: View?) {
        val mainActivity = (activity as MainActivity)
        binding.run {
            when (v?.id) {
                ballImg.id -> mainActivity.changeFragment(4)
                ballText.id -> mainActivity.changeFragment(4)
                leisureImg.id -> mainActivity.changeFragment(5)
                leisureText.id -> mainActivity.changeFragment(5)
                wave.id -> mainActivity.changeFragment(6)
                waterText.id -> mainActivity.changeFragment(6)
                lifeSports.id -> mainActivity.changeFragment(7)
                lifeText.id -> mainActivity.changeFragment(7)
                winterSports.id -> mainActivity.changeFragment(8)
                winterText.id -> mainActivity.changeFragment(8)
                eSports.id -> mainActivity.changeFragment(9)
                gameText.id -> mainActivity.changeFragment(9)
                groupTitle.id -> mainActivity.changeFragment(13).apply {
                    mainActivity.itemSelected()
                }
                groupRecycle.id -> mainActivity.changeFragment(13).apply {
                    mainActivity.itemSelected()
                }
                goGroupBtn.id -> mainActivity.changeFragment(13).apply {
                    mainActivity.itemSelected()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "홈"
    }
}