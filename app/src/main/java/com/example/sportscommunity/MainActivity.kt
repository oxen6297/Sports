package com.example.sportscommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportscommunity.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabIconList = arrayListOf(
            R.drawable.house,
            R.drawable.community,
            R.drawable.shop,
            R.drawable.map,
            R.drawable.reserved
        )

        binding.viewPager.adapter = SportsPagerAdapter(this, 5)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(tabIconList[position])
        }.attach()
    }

    class SportsPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val tabCount: Int
    ) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = tabCount

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SportsHomeFragment()
                1 -> SportsCommunityFragment()
                2 -> SportsShopFragment()
                3 -> SportsMapFragment()
                else -> SportsReservationFragment()
            }

        }
    }

}