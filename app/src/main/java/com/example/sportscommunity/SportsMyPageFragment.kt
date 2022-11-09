package com.example.sportscommunity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsMypageFragmentBinding

class SportsMyPageFragment : Fragment() {

    private var mBinding: SportsMypageFragmentBinding? = null
    private val binding get() = mBinding!!

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var glide: RequestManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsMypageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigationView(false)

        glide = Glide.with(this)

        binding.run {

            addProfileImgBtn.setOnClickListener{
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
                        glide!!.load(imageUri).circleCrop().into(userProfileImg)


                    }
                }



        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "마이페이지"
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}