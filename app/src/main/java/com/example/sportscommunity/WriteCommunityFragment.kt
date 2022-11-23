package com.example.sportscommunity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.WriteCommunityFragmentBinding

class WriteCommunityFragment : Fragment() {

    private var mBinding: WriteCommunityFragmentBinding? = null
    private val binding get() = mBinding!!
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>


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