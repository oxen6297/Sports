package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportscommunity.Adapter.GroupAdapter
import com.example.sportscommunity.Adapter.PlayGroupAdapter
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsMapGroupFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsMapGroupFragment : Fragment() {

    //그룹 모집 탭
    private var mBinding: SportsMapGroupFragmentBinding? = null
    private val binding get() = mBinding!!

    private var flag = 0
    private var flags = 0
    private val group = mutableListOf<GroupPlay>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsMapGroupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigationView(false)

        val playGroupAdapter = PlayGroupAdapter(requireContext(),group)

        callGroup()

        binding.run {

            writeBtn.setOnClickListener {
                mainActivity.changeFragment(0)
                mainActivity.setDataAtFragment(WriteContentFragment(), 1, "writeThree")
            }

            aloneBtn.setOnClickListener {
                mainActivity.changeFragment(2)
            }

            groupSortTime.setOnClickListener {

                if (flag == 0) {
                    groupSortTime.setBackgroundResource(R.drawable.select_background)
                    groupSortTime.setTextColor(Color.WHITE)
                    flag = 1

                } else if (flag == 1) {
                    groupSortTime.setBackgroundResource(R.drawable.edit_text_background)
                    groupSortTime.setTextColor(R.color.black)
                    flag = 0
                }
            }

            groupSortArea.setOnClickListener {
                if (flags == 0) {
                    groupSortArea.setBackgroundResource(R.drawable.select_background)
                    groupSortArea.setTextColor(Color.WHITE)
                    flags = 1
                } else if (flags == 1) {
                    groupSortArea.setBackgroundResource(R.drawable.edit_text_background)
                    groupSortArea.setTextColor(R.color.black)
                    flags = 0
                }
            }


            groupSortCategory.setOnClickListener {

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
                    bottomSheetDialog, bottomSheetView, R.id.sort_ball, groupSortCategory, "구기종목"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_leisure, groupSortCategory, "레저"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_water, groupSortCategory, "해양"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_life, groupSortCategory, "생활"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_winter, groupSortCategory, "동계"
                )
                bottomSheet(
                    bottomSheetDialog,
                    bottomSheetView,
                    R.id.sort_e_sports,
                    groupSortCategory,
                    "이스포츠"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_all, groupSortCategory, "전체"
                )

            }

            groupSortManWoman.setOnClickListener {

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
                    bottomSheetDialog, bottomSheetView, R.id.sort_man, groupSortManWoman, "남자"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_woman, groupSortManWoman, "여자"
                )
                bottomSheet(
                    bottomSheetDialog, bottomSheetView, R.id.sort_all, groupSortManWoman, "전체",
                )
            }

            /**
             * 검색기능
             */

            groupSearchEdit.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    playGroupAdapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable?) {
                    //nothing
                }
            })

//            groupSearchEdit.doOnTextChanged { text, start, before, count ->
//                playGroupAdapter.filter.filter(text)
//            }

            groupSearchBtn.setOnClickListener {
                groupSearchEdit.doAfterTextChanged {
                    playGroupAdapter.filter.filter(it)
                }
            }

            /**
             * 검색기능 여기까지
             */
            playGroupAdapter.setItemClickListener(object : PlayGroupAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    mainActivity.changeFragment(16)
                }
            })
        }
    }

    private fun callGroup() {
        val retrofitService = Retrofits.getGroupPlayService()
        val call: Call<GroupPlayTab> = retrofitService.getGroupPlay()

        call.enqueue(object : Callback<GroupPlayTab> {
            override fun onResponse(call: Call<GroupPlayTab>, response: Response<GroupPlayTab>) {
                try {
                    if (response.isSuccessful) {
                        binding.playWithRecycle.apply {
                            this.adapter =
                                PlayGroupAdapter(requireContext(), response.body()?.groupwrite)
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

            override fun onFailure(call: Call<GroupPlayTab>, t: Throwable) {
                Log.d("failed", "Shop_failed")
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
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

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}