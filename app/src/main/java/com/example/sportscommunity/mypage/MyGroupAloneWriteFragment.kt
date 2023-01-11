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
import com.example.sportscommunity.databinding.FragmentMyGroupWriteBinding
import com.example.sportscommunity.mypageadapter.MyGroupWriteAdapter
import com.example.sportscommunity.repository.Repository
import com.example.sportscommunity.sharedpreference.SharedPreferenceManager
import com.example.sportscommunity.viewmodel.MyPageViewModel
import com.example.sportscommunity.viewmodelfactory.MyPageViewModelFactory

class MyGroupAloneWriteFragment : Fragment() {

    private val myPageViewModel: MyPageViewModel by viewModels { MyPageViewModelFactory(Repository()) }
    private var mBinding: FragmentMyGroupWriteBinding? = null
    private val myWriteAdapter = MyGroupWriteAdapter()
    private var myWriteList = mutableListOf<MyWrite>()
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMyGroupWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postMyPageWrite()
        binding.myBoardRecyclerview.adapter = myWriteAdapter

        val myHash = HashMap<String,Any>()
        val id = SharedPreferenceManager.getInt(requireContext(),"id",0)
        val com = arguments?.getInt("MyWrite", 1) ?: 1

        myHash["id"] = id
        myHash["type"] = com

        myPageViewModel.postMyWriteInfo(myHash)
    }

    private fun postMyPageWrite(){
        myPageViewModel.myWriteResponse.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                myWriteAdapter.getSetData(it.body() as MutableList<MyWrite>)
                //안되면 콜백 인자를 MyWrite로 변경해보고 리스트에 추가해서 셋 데이터 해보기
            }
        }
    }
}