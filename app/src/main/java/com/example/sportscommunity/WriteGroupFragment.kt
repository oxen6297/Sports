package com.example.sportscommunity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.WriteGroupFragmentBinding
import jxl.Sheet
import jxl.Workbook
import java.io.InputStream
import java.util.*

class WriteGroupFragment : Fragment() {

    private var mBinding: WriteGroupFragmentBinding? = null
    private val binding get() = mBinding!!
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var dateString = ""
    private var timeString = ""
    var groupFlag: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WriteGroupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val areaItemTwo = arrayOf("시/군/구", "노원구", "종로구")
        val sexItem = arrayOf("상관없음", "남자", "여자")

        val categoryAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categoryList,
                R.layout.spinner_dropdown_item
            )

        val areaAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.areaList,
                R.layout.spinner_dropdown_item
            )

        val areaAdapterTwo =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, areaItemTwo)

        val sexAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, sexItem)

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        areaAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        areaAdapterTwo.setDropDownViewResource(R.layout.spinner_item_style)
        sexAdapter.setDropDownViewResource(R.layout.spinner_item_style)


        binding.run {

            val activity = activity as MainActivity

            radioGroup.setOnCheckedChangeListener { group, checkedId ->

                when (checkedId) {
                    R.id.two_check_box -> visibleDayOrHour()

                    else -> invisibleDayOrHour()
                }
            }

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

            categorySpinner.adapter = categoryAdapter
            areaSpinner.adapter = areaAdapter
            areaSpinnerTwo.adapter = areaAdapterTwo
            sortSexSpinner.adapter = sexAdapter

            if (categorySpinner.isFocused) {
                Log.d("spinnerr", "success")
            }


            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("spinnerr", binding.categorySpinner.selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            uploadImageBtn.setOnClickListener {
                imagePickerLauncher.launch(Intent(Intent.ACTION_PICK).apply {
                    this.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    this.action = Intent.ACTION_GET_CONTENT
                })
            }

            imagePickerLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    it.data?.data?.let {
                        imageName.text = it.lastPathSegment.toString()
                    }
                }
            saveBtn.setOnClickListener {
                if (oneCheckBox.isChecked) {
                    checkBlank()
                }
                if (twoCheckBox.isChecked) {
                    checkAloneBlank()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun visibleDayOrHour() {
        binding.run {
            dayTitle.visibility = View.VISIBLE
            daySpinner.visibility = View.VISIBLE
            hourSpinner.visibility = View.VISIBLE
            dayCheckBox.visibility = View.VISIBLE
            dayCheckBoxText.visibility = View.VISIBLE
        }
    }

    private fun invisibleDayOrHour() {
        binding.run {
            dayTitle.visibility = View.GONE
            daySpinner.visibility = View.GONE
            hourSpinner.visibility = View.GONE
            dayCheckBox.visibility = View.GONE
            dayCheckBoxText.visibility = View.GONE
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
            } else if (shortTextEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "한줄 설명을 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else if (contentTextEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else if (ageEdit.text.isNullOrBlank() && ageEditTwo.text.isNullOrBlank() && !ageCheckBox.isChecked) {
                Toast.makeText(requireContext(), "나이 범위를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("saveBtnClick", "success")
            }
        }
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

    fun readExcel(context: Context) {
        try {
            val inputStream: InputStream = context.resources.assets.open("sido.xls")
            val wb: Workbook = Workbook.getWorkbook(inputStream)

            val sheet: Sheet = wb.getSheet(0)

            val colTotal: Int = sheet.columns
            val rowIndexStart: Int = 1
            val rowTotal: Int = sheet.getColumn(colTotal - 1).size

            //서울
            for (row in rowIndexStart..rowTotal - 250) {
                for (col in 1..1) {
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Seoul", "row:" + row + "col:" + col + "contents:" + contents)

                    val seoulArray = arrayOf(contents)
                    Log.d("seoulArray", seoulArray.contentToString())
                }
            }

            //경기도
            for (row in rowIndexStart + 25..rowTotal - 207){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Gyungi", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //인천
            for (row in rowIndexStart + 68 .. rowTotal - 197){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Incheon", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //대전
            for (row in rowIndexStart + 78 .. rowTotal - 192){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Daejeon", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //대구
            for (row in rowIndexStart + 83 .. rowTotal - 184){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("DaeGu", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //울산
            for (row in rowIndexStart + 91 .. rowTotal - 179){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Ulsan", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //전남
            for (row in rowIndexStart + 96 .. rowTotal - 157){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Jeonnam", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //전북
            for (row in rowIndexStart + 118 .. rowTotal - 141){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("JeonBuk", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //충남
            for (row in rowIndexStart + 134 .. rowTotal - 125){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Chungnam", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //충북
            for (row in rowIndexStart + 150 .. rowTotal - 111){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Chungbuk", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //제주
            for (row in rowIndexStart + 164 .. rowTotal - 109){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Jeju", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //세종
            for (row in rowIndexStart + 166 .. rowTotal - 86){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Sejong", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //광주
            for (row in rowIndexStart + 189 .. rowTotal - 81){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Gwangju", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //부산
            for (row in rowIndexStart + 194 .. rowTotal - 65){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Busan", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //경북
            for (row in rowIndexStart + 210 .. rowTotal - 41){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Gyungbuk", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //경남
            for (row in rowIndexStart + 234 .. rowTotal - 19){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Gyungnam", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }

            //강원
            for (row in rowIndexStart + 256 .. rowTotal){
                for (col in 1..1){
                    val contents: String = sheet.getCell(col, row).contents
                    Log.d("Gangwon", "row:" + row + "col:" + col + "contents:" + contents)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("errorss", e.toString())
        }
    }
}