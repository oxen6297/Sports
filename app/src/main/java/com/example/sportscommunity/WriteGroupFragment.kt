package com.example.sportscommunity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.WriteGroupFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WriteGroupFragment : Fragment() {

    private var mBinding: WriteGroupFragmentBinding? = null
    private val binding get() = mBinding!!
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var dateString = ""
    private var timeString = ""
    lateinit var callback: OnBackPressedCallback
    private var area = ""
    private var categoryType = 0
    private var dateAndTime = ""
    private var title = ""
    private var shortContent = ""
    private var content = ""
    private var peopleNum = ""
    private var nowNum = ""
    private var sex = ""
    private var nickname = ""
    private var minAge = ""
    private var maxAge = ""
    private var writeTime = ""
    private var image = ""
    private var date = ""
    private var time = ""
    private var once = ""


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

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        val sexItem = arrayOf("상관없음", "남자", "여자")

        val categoryAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sportsList,
                R.layout.spinner_dropdown_item
            )

        val areaAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.areaList,
                R.layout.spinner_dropdown_item
            )


        val sexAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, sexItem)

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        areaAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        sexAdapter.setDropDownViewResource(R.layout.spinner_item_style)


        binding.run {

            categorySpinner.adapter = categoryAdapter
            areaSpinner.adapter = areaAdapter
            sortSexSpinner.adapter = sexAdapter

            areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> detailAreaSpinner(R.array.noSelect)
                        1 -> detailAreaSpinner(R.array.seoulItem)
                        2 -> detailAreaSpinner(R.array.gyungiList)
                        3 -> detailAreaSpinner(R.array.incheonList)
                        4 -> detailAreaSpinner(R.array.daeJeonList)
                        5 -> detailAreaSpinner(R.array.daeGuList)
                        6 -> detailAreaSpinner(R.array.ulsanList)
                        7 -> detailAreaSpinner(R.array.jeonnamList)
                        8 -> detailAreaSpinner(R.array.jeonbukList)
                        9 -> detailAreaSpinner(R.array.chungnamList)
                        10 -> detailAreaSpinner(R.array.chungbukList)
                        11 -> detailAreaSpinner(R.array.jejuList)
                        12 -> detailAreaSpinner(R.array.sejongList)
                        13 -> detailAreaSpinner(R.array.gwangjuList)
                        14 -> detailAreaSpinner(R.array.busanList)
                        15 -> detailAreaSpinner(R.array.gyungbukList)
                        16 -> detailAreaSpinner(R.array.gyungnamList)
                        17 -> detailAreaSpinner(R.array.gangwonList)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

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
                    mainActivity.setDataAtFragmentTwo(this@WriteGroupFragment, dateString, "date")
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
                    mainActivity.setDataAtFragmentTwo(this@WriteGroupFragment, timeString, "time")
                }
                TimePickerDialog(
                    requireContext(),
                    timeSet,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }

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
                        image = it.toString()

                    }
                }
            saveBtn.setOnClickListener {
                if (oneCheckBox.isChecked) {
                    checkBlank()
                    groupRetrofit()
                }
                if (twoCheckBox.isChecked) {
                    checkAloneBlank()
                    groupRetrofit()
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

    private fun detailAreaSpinner(item: Int) {
        binding.run {
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                item,
                R.layout.spinner_dropdown_item
            )
            adapter.setDropDownViewResource(R.layout.spinner_item_style)
            areaSpinnerTwo.adapter = adapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext()).setTitle("맺음")
                    .setMessage("작성을 취소하시겠습니까?\n확인시 작성중이던 글은 삭제됩니다.")
                    .setPositiveButton("확인") { dialog, which ->

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, SportsMapGroupFragment())
                            .commit()
                    }.setNegativeButton("취소") { dialog, which ->
                        dialog.dismiss()
                    }
                builder.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun groupRetrofit() {

        val sp = requireActivity().getSharedPreferences("userId", Context.MODE_PRIVATE)
        val id: Int = sp.getInt("id", 0)

        arguments?.let {
            date = it.getString("date").toString()
            time = it.getString("time").toString()
        }

        when (binding.categorySpinner.selectedItem) {
            "구기종목" -> categoryType = 1
            "레저" -> categoryType = 2
            "해양 스포츠" -> categoryType = 3
            "생활 스포츠" -> categoryType = 4
            "동계 스포츠" -> categoryType = 5
            "E-스포츠" -> categoryType = 6
        }

        area = if (binding.areaCheckBox.isChecked) {
            "상관없음"
        } else {
            binding.areaSpinner.selectedItem.toString() +
                    binding.areaSpinnerTwo.selectedItem.toString()
        }

        dateAndTime = if (binding.dayCheckBox.isChecked) {
            "상관없음"
        } else {
            "$date $time"
        }

        title = binding.contentTitleEdit.text.toString()
        shortContent = binding.shortTextEdit.text.toString()
        content = binding.contentTextEdit.text.toString()
        peopleNum = binding.numberEdit.text.toString()
        nowNum = binding.numberEditTwo.text.toString()
        sex = if (binding.sortSexSpinner.selectedItem == "상관없음") {
            "상관없음"
        } else {
            binding.sortSexSpinner.selectedItem.toString()
        }

        if (binding.ageCheckBox.isChecked) {
            minAge = "상관없음"
            maxAge = "상관없음"
            Log.d("age", minAge)
        } else {
            minAge = binding.ageEdit.text.toString()
            maxAge = binding.ageEditTwo.text.toString()
            Log.d("age", minAge)
        }

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)
        Log.d("currentDate", formatted.toString())

        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)
        Log.d("currentTime", formattedTime.toString())
        writeTime = "$formatted $formattedTime"
        Log.d("currentDateTime", writeTime)

        nickname = sp.getString("nickname","none").toString()

        val writing = HashMap<String, Any>()
        writing["id"] = categoryType
        writing["local"] = area
        writing["date"] = dateAndTime
        writing["title"] = title
        writing["line"] = shortContent
        writing["nickname"] = nickname
        writing["description"] = content
        writing["peoplenum"] = peopleNum
        writing["peoplenownum"] = nowNum
        writing["gender"] = sex
        writing["minage"] = minAge
        writing["maxage"] = maxAge
        writing["writedate"] = writeTime
        writing["titleimage"] = image
        writing["userid"] = 3
        if (binding.oneCheckBox.isChecked){
            writing["once"] = "다회성"
        } else if(binding.twoCheckBox.isChecked){
            writing["once"] = "일회성"
        }



        val retrofitService = Retrofits.postGroup()
        val call: Call<WriteGroupPlay> = retrofitService.postContent(writing)

        call.enqueue(object : Callback<WriteGroupPlay> {
            override fun onResponse(
                call: Call<WriteGroupPlay>,
                response: Response<WriteGroupPlay>
            ) {
                try {
                    if (response.isSuccessful) {
                        Log.e("userInfoPost", "success")
                        Log.d("성공:", "${response.body()}")
                    }
                } catch (e: Exception) {
                    Log.e("userInfoPost", response.body().toString())
                    Log.e("userInfoPost", response.message().toString())
                }
            }

            override fun onFailure(call: Call<WriteGroupPlay>, t: Throwable) {
                Log.e("userInfoPost", t.message.toString())
            }
        })
    }
}