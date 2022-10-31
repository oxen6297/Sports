package com.example.sportscommunity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportscommunity.databinding.SportsHomeFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SportsHomeFragment : Fragment() {

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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(NewsService::class.java)

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
                    } catch (e:Exception){
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<NewsList>, t: Throwable) {
                    Log.d("failed", "failed")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}