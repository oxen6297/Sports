package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsHomeFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsHomeFragment : Fragment(),View.OnClickListener {

    private var mBinding: SportsHomeFragmentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigationView(false)

        val retrofitService = Retrofits.getNewsService()

        val call: Call<NewsList> = retrofitService.getNewsList()

        binding.run {

            ballImg.setOnClickListener(this@SportsHomeFragment)
            ballText.setOnClickListener(this@SportsHomeFragment)
            leisureText.setOnClickListener(this@SportsHomeFragment)
            leisureImg.setOnClickListener(this@SportsHomeFragment)
            lifeSports.setOnClickListener(this@SportsHomeFragment)
            lifeText.setOnClickListener(this@SportsHomeFragment)
            wave.setOnClickListener(this@SportsHomeFragment)
            waterText.setOnClickListener(this@SportsHomeFragment)
            winterSports.setOnClickListener(this@SportsHomeFragment)
            winterText.setOnClickListener(this@SportsHomeFragment)
            eSports.setOnClickListener(this@SportsHomeFragment)
            gameText.setOnClickListener(this@SportsHomeFragment)
            groupTitle.setOnClickListener(this@SportsHomeFragment)
            groupRecycle.setOnClickListener(this@SportsHomeFragment)


            call.enqueue(object : Callback<NewsList> {
                override fun onResponse(
                    call: Call<NewsList>,
                    response: Response<NewsList>
                ) {

                    try {
                        if (response.isSuccessful) {

                            newsRecycle.apply {
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
    }

    override fun onClick(v: View?) {
        val mainActivity = (activity as MainActivity)
        binding.run {
            when(v?.id){
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

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}