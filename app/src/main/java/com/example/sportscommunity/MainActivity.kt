package com.example.sportscommunity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.example.sportscommunity.community.FreeCategoryFragment
import com.example.sportscommunity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

    }

    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, SportsHomeFragment()).commitAllowingStateLoss()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, SportsHomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.community -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, SportsCommunityFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.shop -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, SportsShopFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, SportsMapGroupFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, SportsReservationFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
        }
    }

    fun changeFragment(index: Int) {
        when (index) {
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, FreeCategoryFragment()).commit()
            }
            0 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WriteContentFragment()).commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapFragment()).commit()
            }
            3-> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapGroupFragment()).commit()
            }
        }
    }

    fun hideBottomNavigationView(hide: Boolean) {
        if (hide) {
            binding.bottomNav.visibility = View.GONE
        }
    }

    fun showOptionMenu(boolean: Boolean) {

    }


}