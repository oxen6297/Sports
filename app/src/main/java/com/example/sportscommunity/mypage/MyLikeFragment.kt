package com.example.sportscommunity.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sportscommunity.MyWrite
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.FragmentMyGrouopPageBinding
import com.example.sportscommunity.databinding.FragmentMyLikeBinding
import com.example.sportscommunity.mypageadapter.MyLikeAdapter
import com.example.sportscommunity.repository.Repository
import com.example.sportscommunity.sharedpreference.SharedPreferenceManager
import com.example.sportscommunity.viewmodel.MyPageViewModel
import com.example.sportscommunity.viewmodelfactory.MyPageViewModelFactory


class MyLikeFragment : Fragment() {

    private var mBinding: FragmentMyLikeBinding? = null
    private val binding get() = mBinding!!
    private val myPageViewModel: MyPageViewModel by viewModels { MyPageViewModelFactory(Repository()) }
    private val myLikeAdapter = MyLikeAdapter()
    private var myLikeList = mutableListOf<MyWrite>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMyLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postMyPageLike()
        binding.myLikeRecyclerview.adapter = myLikeAdapter

        val myHash = HashMap<String,Any>()
        val id = SharedPreferenceManager.getInt(requireContext(),"id",0)
        val com = arguments?.getInt("MyWrite", 1) ?: 1

        myHash["id"] = id
        myHash["type"] = com

        myPageViewModel.postMyLikeInfo(myHash)
    }

    private fun postMyPageLike(){
        myPageViewModel.myLikeResponse.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                myLikeAdapter.getSetData(it.body() as MutableList<MyWrite>)
                //????????? ?????? ????????? MyWrite??? ??????????????? ???????????? ???????????? ??? ????????? ?????????
            }
        }
    }
}