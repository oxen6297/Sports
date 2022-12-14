package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sportscommunity.databinding.AloneBoardFragmentBinding

class AloneBoardFragment : Fragment() {

    lateinit var binding: AloneBoardFragmentBinding
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.alone_board_fragment, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        binding.run {
            //제목, 닉네임, 지역, 시간, 카테고리, 제한(성별, 나이), 내용, 프사
            val titles = titleHash["title"].toString()
            val categorys = categoryHash["category"].toString()
            val locals = localHash["local"].toString()
            val descriptions = descriptionHash["description"].toString()
            val genders = genderHash["gender"].toString()
            val minages = minageHash["minage"].toString()
            val maxages = maxageHash["maxage"].toString()
            val images = userImageHash["image"].toString()
            val nicknames = nicknameHash["nickname"].toString()
            val writedates = writedateHash["writedate"].toString()

            playTitle.text = titles
            playCategory.text = categorys
            userArea.text = locals
            userComment.text = descriptions
            userSelectManWoman.text = genders
            age.text = minages + "세" + " ~ " + maxages + "세"
            Glide.with(requireContext()).load(images).fitCenter().error(R.color.orange)
                .into(userProfileImg)
            userId.text = nicknames
            time.text = writedates

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapFragment())
                    .commit()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}