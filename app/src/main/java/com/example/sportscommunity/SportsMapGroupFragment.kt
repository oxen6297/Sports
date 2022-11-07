package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsMapGroupFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SportsMapGroupFragment : Fragment() {

    //그룹 모집 탭

    private var mBinding: SportsMapGroupFragmentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsMapGroupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.select_map_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.options_menu -> {

                        val mainActivity = (activity as MainActivity)

                        val bottomSheetDialog = BottomSheetDialog(requireContext())
                        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                            R.layout.bottom_sheet_dialog,
                            view.findViewById(R.id.bottom_sheet) as LinearLayout?
                        )

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "함께해요"

    }
}