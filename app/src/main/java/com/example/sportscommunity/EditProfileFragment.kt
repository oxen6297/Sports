package com.example.sportscommunity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.sportscommunity.databinding.EditProfileFragmentBinding

class EditProfileFragment : Fragment() {

    private var mBinding: EditProfileFragmentBinding? = null
    private val binding get() = mBinding!!
    lateinit var callback: OnBackPressedCallback
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var glide: RequestManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = EditProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("FragmentBackPressedCallback")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        glide = Glide.with(this)

        binding.run {

            backPress.setOnClickListener {
                dialog(requireContext())
            }

            editProfileBtn.setOnClickListener {
                imagePickerLauncher.launch(
                    Intent(Intent.ACTION_PICK).apply {
                        this.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        this.action = Intent.ACTION_GET_CONTENT
                    }
                )
            }
            imagePickerLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    it.data?.data?.let { uri ->

                        val imageUri: Uri? = it.data?.data
                        glide!!.load(imageUri).circleCrop().into(profileImg)
                    }
                }
        }

        val sexItem = arrayOf("설정안함", "남자", "여자")

        val categoryAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sportsList,
                R.layout.spinner_dropdown_item
            )

        val sexAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, sexItem)

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        sexAdapter.setDropDownViewResource(R.layout.spinner_item_style)

        binding.run {
            sortGenderSpinner.adapter = sexAdapter
            sortLikeSpinner.adapter = categoryAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialog(requireContext())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun dialog(context: Context){
        val builder = AlertDialog.Builder(requireContext()).setTitle("맺음")
            .setMessage("작성을 취소하시겠습니까?\n확인시 작성중이던 정보는 삭제됩니다.")
            .setPositiveButton("확인") { dialog, which ->

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMyPageFragment())
                    .commit()
            }.setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
        builder.show()
    }
}