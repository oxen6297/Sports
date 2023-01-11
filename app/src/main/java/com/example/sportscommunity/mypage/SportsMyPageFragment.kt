package com.example.sportscommunity.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.adapter.backPressed
import com.example.sportscommunity.databinding.SportsMypageFragmentBinding

class SportsMyPageFragment : Fragment() {

    private var mBinding: SportsMypageFragmentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsMypageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigationView(false)
        val myWriteFragment = MyGroupAloneWriteFragment()
        val myLikeFragment = MyLikeFragment()

        val sp = requireActivity().getSharedPreferences("userId", Context.MODE_PRIVATE)
        val nickname = sp.getString("nickname", "닉네임").toString()

        binding.run {
            editProfileBtn.setOnClickListener {
                mainActivity.changeFragment(15)
            }
            userId.text = nickname

            writeBoard.setOnClickListener {
                mainActivity.changeFragment(21)
                mainActivity.setDataAtFragment(myWriteFragment,1,"myWrite")
            }
            writeBoardCom.setOnClickListener {
                mainActivity.changeFragment(21)
                mainActivity.setDataAtFragment(myWriteFragment,2,"myWrite")
            }
            writeBoardShop.setOnClickListener {
                mainActivity.changeFragment(21)
                mainActivity.setDataAtFragment(myWriteFragment,3,"myWrite")
            }
            likeList.setOnClickListener {
                mainActivity.changeFragment(23)
                mainActivity.setDataAtFragment(myLikeFragment,1,"myLike")
            }
            likeListCom.setOnClickListener {
                mainActivity.changeFragment(23)
                mainActivity.setDataAtFragment(myLikeFragment,2,"myLike")
            }
            likeListShop.setOnClickListener {
                mainActivity.changeFragment(23)
                mainActivity.setDataAtFragment(myLikeFragment,3,"myLike")
            }
            verification.setOnClickListener {
                mainActivity.changeFragment(22)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}