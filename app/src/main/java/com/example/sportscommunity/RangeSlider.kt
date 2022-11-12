package com.example.sportscommunity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.SortNumberBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider

class RangeSlider : BottomSheetDialogFragment() {

    private var mBinding: SortNumberBottomSheetDialogBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SortNumberBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            @SuppressLint("SetTextI18n")
            override fun onStopTrackingTouch(slider: RangeSlider) {

                Log.d("range2", slider.values[0].toString())
                Log.d("range3", slider.values[1].toString())

                binding.minText.text = "최소 인원: " + slider.values[0].toInt().toString() + "명"
                binding.maxText.text = "최대 인원: " + slider.values[1].toInt().toString() + "명"

                binding.saveBtn.setOnClickListener {
                    val mainActivity = activity as MainActivity
                    mainActivity.setDataAtFragment(SportsMapGroupFragment(),slider.values[0].toInt())
                    mainActivity.setDataAtFragmentTwo(SportsMapGroupFragment(),slider.values[1].toInt())
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun getTheme(): Int = R.style.BottomSheetDialog
}