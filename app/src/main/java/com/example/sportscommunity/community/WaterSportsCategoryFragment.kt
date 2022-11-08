package com.example.sportscommunity.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.databinding.WaterSportsCategoryTabBinding

class WaterSportsCategoryFragment: Fragment() {

    private var mBinding: WaterSportsCategoryTabBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WaterSportsCategoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)

        mainActivity.hideBottomNavigationView(true)

    }
}