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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.WriteCommunityFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WriteCommunityFragment : Fragment() {

    private var mBinding: WriteCommunityFragmentBinding? = null
    private val binding get() = mBinding!!
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    lateinit var callback: OnBackPressedCallback
    private var categoryType = 0
    private var title = ""
    private var content = ""
    private var image = ""
    private var writeTime = ""


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

        val categoryAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categoryList,
                R.layout.spinner_dropdown_item
            )

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)

        binding.run {
            val mainActivity = activity as MainActivity
            categorySpinner.adapter = categoryAdapter


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
                        mainActivity.setDataAtFragmentTwo(
                            this@WriteCommunityFragment,
                            it.toString(),
                            "title"
                        )
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
        arguments?.let {
            image = it.getInt("title").toString()
        }

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

        title = binding.contentTitleEdit.text.toString()
        content = binding.contentTextEdit.text.toString()
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)
        Log.d("currentDate", formatted.toString())

        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)
        Log.d("currentTime", formattedTime.toString())

        writeTime = "$formatted $formattedTime"
        Log.d("currentDateTime", writeTime)

        val user = HashMap<String, Any>()
        user["type"] = categoryType
        user["title"] = title
        user["content"] = content
        user["image"] = image
        user["time"] = writeTime

        val retrofitService = Retrofits.postCommunity()
        val call: Call<WriteContent> = retrofitService.postContent(user)

        call.enqueue(object : Callback<WriteContent> {
            override fun onResponse(
                call: Call<WriteContent>,
                response: Response<WriteContent>
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

            override fun onFailure(call: Call<WriteContent>, t: Throwable) {
                Log.e("userInfoPost", t.message.toString())
            }
        })
    }
}