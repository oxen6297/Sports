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

class WriteCommunityFragment : Fragment() {

    private var mBinding: WriteCommunityFragmentBinding? = null
    private val binding get() = mBinding!!
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    lateinit var callback: OnBackPressedCallback



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
                    }
                }

            saveBtn.setOnClickListener {
                checkBlank()
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
}