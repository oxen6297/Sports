package com.example.sportscommunity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.sportscommunity.Repository.Repository
import com.example.sportscommunity.ViewModel.MainViewModel
import com.example.sportscommunity.ViewModelFactory.MainViewModelFactory
import com.example.sportscommunity.databinding.WriteCommunityFragmentBinding
import com.kakao.usermgmt.StringSet.email
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class WriteCommunityFragment : Fragment() {

    private var mBinding: WriteCommunityFragmentBinding? = null
    private val binding get() = mBinding!!
    private val mainViewModel: MainViewModel by viewModels { MainViewModelFactory(Repository()) }
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    lateinit var callback: OnBackPressedCallback
    private var categoryType = 0
    private var title = ""
    private var content = ""
    private var nickname = ""
    private var image = ""
    private var writeTime = ""
    private var flag = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WriteCommunityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flag = writeFlag["write"].toString().toInt()

        val categoryAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categoryList,
                R.layout.spinner_dropdown_item
            )

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)

        binding.run {
            categorySpinner.adapter = categoryAdapter

            autoSelectCategory(flag, binding.categorySpinner)

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
                checkBlank()
                communityRetrofit()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext()).setTitle("맺음")
                    .setMessage("작성을 취소하시겠습니까?\n확인시 작성중이던 글은 삭제됩니다.")
                    .setPositiveButton("확인") { dialog, which ->


                        val mainActivity = activity as MainActivity
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, SportsHomeFragment())
                            .commit()
                        mainActivity.homeSelected()

                    }.setNegativeButton("취소") { dialog, which ->
                        dialog.dismiss()
                    }
                builder.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun checkBlank() {
        binding.run {
            if (categorySpinner.selectedItem.toString() == "선택") {
                Toast.makeText(requireContext(), "카테고리를 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else if (contentTitleEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (contentTextEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Log.d("saveBtnClick", "success")
            }
        }
    }

    private fun communityRetrofit() {

        val sp = requireActivity().getSharedPreferences("userId", Context.MODE_PRIVATE)
        val id: Int = sp.getInt("id", 0)

        when (binding.categorySpinner.selectedItem) {
            "구기종목" -> categoryType = 1
            "레저" -> categoryType = 2
            "해양 스포츠" -> categoryType = 3
            "생활 스포츠" -> categoryType = 4
            "동계 스포츠" -> categoryType = 5
            "E-스포츠" -> categoryType = 6
            "자유게시판" -> categoryType = 7
            "비밀게시판" -> categoryType = 8
            "질문게시판" -> categoryType = 9
            "문의하기" -> categoryType = 10
        }


        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)
        Log.d("currentDate", formatted.toString())

        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)
        Log.d("currentTime", formattedTime.toString())

        writeTime = "$formatted $formattedTime"
        title = binding.contentTitleEdit.text.toString()
        content = binding.contentTextEdit.text.toString()
        nickname = sp.getString("nickname", "none").toString()

        val comwrites = HashMap<String, Any>()
        comwrites["title"] = title
        comwrites["description"] = content
        comwrites["img"] = image
        comwrites["id"] = categoryType.toString().toInt()
        comwrites["nickname"] = nickname
        comwrites["userid"] = 3
        comwrites["writedate"] = writeTime
        comwrites["likedcount"] = 0

        mainViewModel.writeCommunity.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Log.d("writeCom", "success")
            } else {
                Log.d("writeCom", it.errorBody().toString())
            }
        }
        mainViewModel.writeCommunity(comwrites)
    }

    private fun autoSelectCategory(flag: Int, spinner: Spinner) {
        when (flag) {
            flag -> {
                spinner.setSelection(flag)
            }
        }
    }
}