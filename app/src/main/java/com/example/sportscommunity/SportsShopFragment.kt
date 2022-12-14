package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportscommunity.Adapter.ShopAdapter
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsShopFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsShopFragment : Fragment() {

    private var mBinding: SportsShopFragmentBinding? = null
    private val binding get() = mBinding!!
    private var flag = 0
    private var flags = 0
    private val shop = mutableListOf<Shop>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsShopFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callShop()

        arguments?.let {
            flags = it.getInt("flags")
        }

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigationView(false)

        val shopAdapter = ShopAdapter(requireContext(), shop, mainActivity)

        binding.run {

            shopRecycle.scrollToPosition(shop.size-1)
            writeBtn.setOnClickListener {
                mainActivity.changeFragment(14)
            }

            shopSortTime.setOnClickListener {
                if (flag == 0) {
                    shopSortTime.setBackgroundResource(R.drawable.select_background)
                    shopSortTime.setTextColor(Color.WHITE)
                    flag = 1

                } else if (flag == 1) {
                    shopSortTime.setBackgroundResource(R.drawable.edit_text_background)
                    shopSortTime.setTextColor(R.color.black)
                    flag = 0
                }
            }

            shopSortCategory.setOnClickListener {
                val bottomSheetDialog =
                    BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
                val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                    R.layout.sort_group_bottom_sheet_dialog,
                    view.findViewById(R.id.group_sort_bottom_sheet) as LinearLayout?,
                    false
                )

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_ball, shopSortCategory, "구기종목"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_leisure, shopSortCategory, "레저"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_water, shopSortCategory, "해양"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_life, shopSortCategory, "생활"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_winter, shopSortCategory, "동계"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_e_sports, shopSortCategory, "이스포츠"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_all, shopSortCategory, "전체"
                )
            }

            val bottomSheetDialogFragment = ShopBottomSheetDialog()

            shopSortArea.setOnClickListener {
                bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
                if (flags == 0) {
                    shopSortArea.setBackgroundResource(R.drawable.select_background)
                    shopSortArea.setTextColor(Color.WHITE)

                } else if (flags == 1) {
                    shopSortArea.setBackgroundResource(R.drawable.edit_text_background)
                    shopSortArea.setTextColor(R.color.black)
                }
            }

            /**
             * 검색기능
             */
            shopSearchEdit.doOnTextChanged { text, start, before, count ->
                shopAdapter.filter.filter(text)
            }

            shopSearchBtn.setOnClickListener {
                shopSearchEdit.doAfterTextChanged {
                    shopAdapter.filter.filter(it)
                }
            }
            /**
             * 검색기능 여기까지
             */
        }
    }

    private fun callShop() {
        val retrofitService = Retrofits.getShopService()
        val call: Call<ShopTab> = retrofitService.getShop()
        val mainActivity = (activity as MainActivity)

        call.enqueue(object : Callback<ShopTab> {
            override fun onResponse(call: Call<ShopTab>, response: Response<ShopTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.shopRecycle.apply {
                            this.adapter =
                                ShopAdapter(
                                    requireContext(),
                                    response.body()?.usedwrite,
                                    mainActivity
                                )
                            this.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                true
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ShopTab>, t: Throwable) {
                Log.d("failed", "Shop_failed")
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "중고거래"
    }

    private fun bottomSheet(
        bottomSheetDialog: BottomSheetDialog,
        view: View,
        intTree: Int,
        textView: TextView,
        text: String
    ) {

        view.findViewById<View>(intTree).setOnClickListener {
            textView.text = text
            textView.setTextColor(Color.WHITE)
            textView.setBackgroundResource(R.drawable.select_background)
            bottomSheetDialog.dismiss()
        }
    }
}