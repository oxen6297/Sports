package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsMapFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SportsMapFragment : Fragment() {

    //개인 탭
    private var mBinding: SportsMapFragmentBinding? = null
    private val binding get() = mBinding!!

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

        binding.run {
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
                bottomSheet(bottomSheetDialog, bottomSheetView, R.id.sort_e_sports, sortCategory, "이스포츠",
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
        }



        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.select_map_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.options_menu -> {

                        val bottomSheetDialog = BottomSheetDialog(requireContext())
                        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                            R.layout.bottom_sheet_dialog,
                            view.findViewById(R.id.bottom_sheet)
                        )
                        bottomSheetView.setBackgroundColor(Color.TRANSPARENT)

                        bottomSheetView.findViewById<View>(R.id.alone_frag).setOnClickListener {
                            mainActivity.changeFragment(2)
                            bottomSheetDialog.dismiss()
                        }

                        bottomSheetView.findViewById<View>(R.id.group_frag).setOnClickListener {
                            mainActivity.changeFragment(3)
                            bottomSheetDialog.dismiss()
                        }

                        bottomSheetDialog.setContentView(bottomSheetView)
                        bottomSheetDialog.show()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "함께해요 (개인)"
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}