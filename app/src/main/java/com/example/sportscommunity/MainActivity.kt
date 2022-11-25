package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sportscommunity.community.*
import com.example.sportscommunity.databinding.ActivityMainBinding
import jxl.Sheet
import jxl.Workbook
import java.io.InputStream
import java.lang.StringBuilder
import java.util.*

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
                        .replace(R.id.fragment_container_view, SportsMyPageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
        }
    }

    fun changeFragment(index: Int) {
        when (index) {
            0 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WriteContentFragment())
                    .addToBackStack(null).commit()
            }
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, FreeCategoryFragment())
                    .addToBackStack(null).commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapFragment())
                    .addToBackStack(null).commit()
            }
            3 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapGroupFragment())
                    .addToBackStack(null).commit()
            }
            4 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, BallSportsCategoryFragment())
                    .addToBackStack("ball").commit()
            }
            5 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LeisureCategoryFragment())
                    .addToBackStack("leisure").commit()
            }
            6 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WaterSportsCategoryFragment())
                    .addToBackStack("water").commit()
            }
            7 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LifeSportsCategoryFragment())
                    .addToBackStack("life").commit()
            }
            8 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WinterSportsCategoryFragment())
                    .addToBackStack("winter").commit()
            }
            9 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, ESportsCategoryFragment())
                    .addToBackStack("e_sports").commit()
            }
            10 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SecretCategoryFragment())
                    .addToBackStack(null).commit()
            }
            11 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, QuestionCategoryFragment())
                    .addToBackStack(null).commit()
            }
            12 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, FaQCategoryFragment())
                    .addToBackStack(null).commit()
            }
            13 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapGroupFragment())
                    .addToBackStack(null).commit()
            }
        }
    }

    fun changeWriteFragment(index: Int) {
        when (index) {
            0 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.write_fragment_container_view, WriteGroupFragment())
                    .addToBackStack(null).commit()
            }
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.write_fragment_container_view, WriteAloneFragment())
                    .addToBackStack(null).commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.write_fragment_container_view, WriteCommunityFragment())
                    .addToBackStack(null).commit()
            }
        }
    }

    fun hideBottomNavigationView(hide: Boolean) {
        if (hide) {
            binding.bottomNav.visibility = View.GONE
        } else {
            binding.bottomNav.visibility = View.VISIBLE
        }
    }

    fun itemSelected() {
        binding.bottomNav.selectedItemId = R.id.map
    }

    fun setDataAtFragment(fragment: Fragment, title: Int) {
        val bundle = Bundle()
        bundle.putInt("title", title)

        fragment.arguments = bundle
        setFragment(fragment)
    }

    fun setDataAtFragmentTwo(fragment: Fragment, title: Int) {
        val bundle = Bundle()
        bundle.putInt("titles", title)

        fragment.arguments = bundle
        setFragment(fragment)
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view, fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}
