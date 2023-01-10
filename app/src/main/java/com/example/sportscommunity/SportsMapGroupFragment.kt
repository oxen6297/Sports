package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sportscommunity.adapter.PlayGroupAdapter
import com.example.sportscommunity.adapter.backPressed
import com.example.sportscommunity.repository.Repository
import com.example.sportscommunity.viewmodel.MainViewModel
import com.example.sportscommunity.viewmodelfactory.MainViewModelFactory
import com.example.sportscommunity.databinding.SportsMapGroupFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SportsMapGroupFragment : Fragment() {

    //그룹 모집 탭
    private var mBinding: SportsMapGroupFragmentBinding? = null
    private val mainViewModel: MainViewModel by viewModels {MainViewModelFactory(Repository())}
    private val binding get() = mBinding!!
    private lateinit var playGroupAdapter: PlayGroupAdapter

    private var flag = 0
    private var flags = 0
    private var homeFlag = 0
    private val group = mutableListOf<GroupPlay>()
    var activitys: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsMapGroupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        getGroup(mainActivity)


        activitys?.hideBottomNavigationView(false)

        arguments?.let {
            homeFlag = it.getInt("alone")
        }
        if (homeFlag == 1) {
            activitys?.changeFragment(20)
        }


        binding.run {

            groupSearchEdit.doAfterTextChanged {
                playGroupAdapter.filter.filter(it.toString())
            }

            aloneBtn.setOnClickListener {
                activitys?.changeFragment(2)
            }

            writeBtn.setOnClickListener {
                activitys?.changeFragment(0)
                activitys?.setDataAtFragment(WriteContentFragment(), 1, "writeThree")
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
        }
    }

    private fun getGroup(mainActivity: MainActivity){
        mainViewModel.groupResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                playGroupAdapter = PlayGroupAdapter(
                    requireContext(),
                    it.body()?.groupwrite,
                    mainActivity
                )
                binding.playWithRecycle.adapter = playGroupAdapter
                binding.playWithRecycle.setHasFixedSize(true)
            } else {
                Log.d("GroupError", it.errorBody().toString())
            }
        }
        mainViewModel.getGroup()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
        activitys = activity as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        activitys = null
    }

    override fun onResume() {
        super.onResume()
        binding.playWithRecycle.scrollToPosition(group.size - 1)
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