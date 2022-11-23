package com.example.sportscommunity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.WriteAloneFragmentBinding
import java.util.*

class WriteAloneFragment : Fragment() {

    private var mBinding: WriteAloneFragmentBinding? = null
    private val binding get() = mBinding!!
    private var dateString = ""
    private var timeString = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WriteAloneFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val areaItemTwo = arrayOf("시/군/구", "노원구", "종로구")
        val sexItem = arrayOf("상관없음", "남자", "여자")

        val categoryAdapter =
            ArrayAdapter.createFromResource(requireContext(),R.array.categoryList,R.layout.spinner_dropdown_item)

        val areaAdapter =
            ArrayAdapter.createFromResource(requireContext(),R.array.areaList,R.layout.spinner_dropdown_item)

        val areaAdapterTwo =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, areaItemTwo)

        val sexAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, sexItem)

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        areaAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        areaAdapterTwo.setDropDownViewResource(R.layout.spinner_item_style)
        sexAdapter.setDropDownViewResource(R.layout.spinner_item_style)

        binding.run {
            categorySpinner.adapter = categoryAdapter
            areaSpinner.adapter = areaAdapter
            areaSpinnerTwo.adapter = areaAdapterTwo
            sortSexSpinner.adapter = sexAdapter

            daySpinner.setOnClickListener {
                val calendar = Calendar.getInstance()
                val dateSet = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    dateString = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                    daySpinner.text = dateString
                    daySpinner.setTextColor(Color.parseColor("#1F1F1F"))
                }
                DatePickerDialog(
                    requireContext(),
                    dateSet,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            hourSpinner.setOnClickListener {
                val calendar = Calendar.getInstance()
                val timeSet = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                    timeString = "${hourOfDay}시 ${minute}분"
                    hourSpinner.text = timeString
                    hourSpinner.setTextColor(Color.parseColor("#1F1F1F"))
                }
                TimePickerDialog(
                    requireContext(),
                    timeSet,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }

            saveBtn.setOnClickListener {
                checkAloneBlank()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun checkAloneBlank() {
        checkBlank()
        binding.run {
            if (!dayCheckBox.isChecked) {
                if (daySpinner.text == "날짜를 선택해주세요.") {
                    Toast.makeText(requireContext(), "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else if (hourSpinner.text == "시간을 선택해주세요.") {
                    Toast.makeText(requireContext(), "시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("SaveBtnClicked", "success")
                }
            }
            if (dayCheckBox.isChecked) {
                Log.d("SaveBtnClicked", "success")
            }
        }
    }

    private fun checkBlank() {
        binding.run {
            if (categorySpinner.selectedItem.toString() == "선택") {
                Toast.makeText(requireContext(), "카테고리를 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else if (areaSpinner.selectedItem.toString() == "시/도" && !areaCheckBox.isChecked) {
                Toast.makeText(requireContext(), "지역을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (contentTitleEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
             else if (contentTextEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else if (ageEdit.text.isNullOrBlank() && ageEditTwo.text.isNullOrBlank() && !ageCheckBox.isChecked) {
                Toast.makeText(requireContext(), "나이 범위를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("saveBtnClick", "success")
            }
        }
    }
}