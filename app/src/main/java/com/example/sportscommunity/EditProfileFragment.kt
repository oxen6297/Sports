package com.example.sportscommunity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.sportscommunity.Repository.Repository
import com.example.sportscommunity.viewmodel.LoginAndSignViewModel
import com.example.sportscommunity.ViewModelFactory.LoginAndSignViewModelFactory
import com.example.sportscommunity.databinding.EditProfileFragmentBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditProfileFragment : Fragment() {

    lateinit var binding: EditProfileFragmentBinding
    lateinit var callback: OnBackPressedCallback
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var glide: RequestManager? = null
    private val userInfoViewModel: LoginAndSignViewModel by viewModels {
        LoginAndSignViewModelFactory(
            Repository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.edit_profile_fragment, container, false)
        return binding.root
    }

    @SuppressLint("FragmentBackPressedCallback")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = userInfoViewModel
        binding.lifecycleOwner = this
        val sp = requireActivity().getSharedPreferences("userId", Context.MODE_PRIVATE)
        val id = sp.getInt("id",0)

        userInfoViewModel.userId.value = id

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        glide = Glide.with(this)

        val categoryAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sportsList,
                R.layout.spinner_dropdown_item
            )

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)

        binding.run {

            sortLikeSpinner.adapter = categoryAdapter
            sortLikeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    userInfoViewModel.inputLikeCategory.value =
                        sortLikeSpinner.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
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
                        userInfoViewModel.userProfileImage.value = imageUri.toString()
                    }
                }

            backPress.setOnClickListener {
                dialog()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun dialog() {
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