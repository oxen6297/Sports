package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportscommunity.Adapter.CommunityAdapter
import com.example.sportscommunity.Adapter.PlayGroupAdapter
import com.example.sportscommunity.community.*
import com.example.sportscommunity.databinding.ActivityMainBinding
import jxl.Sheet
import jxl.Workbook
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, FreeCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            3 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapGroupFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            4 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, BallSportsCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            5 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LeisureCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            6 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WaterSportsCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            7 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LifeSportsCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            8 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WinterSportsCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            9 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, ESportsCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            10 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SecretCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            11 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, QuestionCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            12 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, FaQCategoryFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            13 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapGroupFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            14 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WriteShopFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            15 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, WriteShopFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
        }
    }

    fun changeWriteFragment(index: Int) {
        when (index) {
            0 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.write_fragment_container_view, WriteGroupFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.write_fragment_container_view, WriteAloneFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.write_fragment_container_view, WriteCommunityFragment())
                    .setReorderingAllowed(true)
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

    fun homeSelected() {
        binding.bottomNav.selectedItemId = R.id.home
    }

    fun setDataAtFragment(fragment: Fragment, title: Int, name: String) {
        val bundle = Bundle()
        bundle.putInt(name, title)

        fragment.arguments = bundle
        setFragment(fragment)
    }

    fun setDataAtFragmentTwo(fragment: Fragment, title: String, name: String) {
        val bundle = Bundle()
        bundle.putString(name, title)

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

    fun callCommunity(recyclerView: RecyclerView, flag: Int) {
        val retrofitService = Retrofits.getCommunityService()
        val call: Call<CommunityTab> = retrofitService.getCommunity()

        call.enqueue(object : Callback<CommunityTab> {
            override fun onResponse(call: Call<CommunityTab>, response: Response<CommunityTab>) {
                try {
                    if (response.isSuccessful) {
                        recyclerView.apply {
                            this.adapter = CommunityAdapter(
                                this@MainActivity,
                                response.body()?.community,
                                flag
                            )
                            this.layoutManager = LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<CommunityTab>, t: Throwable) {
                Log.d("failed", "Shop_failed")
            }
        })
    }
}
