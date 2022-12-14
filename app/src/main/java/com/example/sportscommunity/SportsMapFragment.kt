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
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportscommunity.Adapter.PlayWithAdapter
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsMapFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsMapFragment : Fragment() {

    //개인 탭
    private var mBinding: SportsMapFragmentBinding? = null
    private val binding get() = mBinding!!
    private val alone = mutableListOf<PlayWith>()

    private var flags = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsMapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigationView(false)

        val aloneAdapter = PlayWithAdapter(requireContext(), alone, mainActivity)

        callAlone()

        binding.run {

            groupPlayWithRecycle.scrollToPosition(alone.size-1)
            groupBtn.setOnClickListener {
                mainActivity.changeFragment(3)
            }

            sortFar.setOnClickListener {
                if (flags == 0) {
                    sortFar.setBackgroundResource(R.drawable.select_background)
                    sortFar.setTextColor(Color.WHITE)
                    flags = 1

                } else if (flags == 1) {
                    sortFar.setBackgroundResource(R.drawable.edit_text_background)
                    sortFar.setTextColor(R.color.black)
                    flags = 0
                }
            }

            writeBtn.setOnClickListener {
                mainActivity.changeFragment(0)
                mainActivity.setDataAtFragment(WriteContentFragment(), 1, "writeTwo")
            }

            sortCategory.setOnClickListener {
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
                    bottomSheetDialog, bottomSheetView, R.id.sort_ball, sortCategory, "구기종목",
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_leisure, sortCategory, "레저",
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_water, sortCategory, "해양",
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_life, sortCategory, "생활",
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_winter, sortCategory, "동계",
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_e_sports, sortCategory, "이스포츠",
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_all, sortCategory, "전체",
                )
            }

            sortManWoman.setOnClickListener {
                val bottomSheetDialog =
                    BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
                val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                    R.layout.sort_man_bottom_sheet_dialog,
                    view.findViewById(R.id.man_sort_bottom_sheet) as LinearLayout?,
                    false
                )

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_man, sortManWoman, "남자"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_woman, sortManWoman, "여자"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_all, sortManWoman, "전체",
                )
            }

            /**
             * 검색기능
             */
            searchEdit.doOnTextChanged { text, start, before, count ->
                aloneAdapter.filter.filter(text)
            }

            searchBtn.setOnClickListener {
                searchEdit.doAfterTextChanged {
                    aloneAdapter.filter.filter(it)
                }
            }
            /**
             * 여기까지
             */
        }
    }

    private fun callAlone() {
        val retrofitService = Retrofits.getAlonePlayService()
        val call: Call<AlonePlay> = retrofitService.getAlonePlay()
        val mainActivity = (activity as MainActivity)

        call.enqueue(object : Callback<AlonePlay> {
            override fun onResponse(call: Call<AlonePlay>, response: Response<AlonePlay>) {
                try {
                    if (response.isSuccessful) {
                        binding.groupPlayWithRecycle.apply {
                            this.adapter =
                                PlayWithAdapter(
                                    requireContext(),
                                    response.body()?.individualwrite,
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

            override fun onFailure(call: Call<AlonePlay>, t: Throwable) {
                Log.d("failed", "Shop_failed")
            }
        })
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}