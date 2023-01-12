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
import com.example.sportscommunity.databinding.GroupBoardFragmentBinding
import com.example.sportscommunity.sharedpreference.SharedPreferenceManager

class GroupBoardFragment : Fragment() {

    lateinit var binding: GroupBoardFragmentBinding
    lateinit var callback: OnBackPressedCallback
    private var database: UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_board_fragment, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = UserDatabase.getInstance(requireContext())

        val m = "M"
        val f = "F"
        val isParticipate =
            SharedPreferenceManager.getString(requireContext(), "isParticipate", "no")

        if (isParticipate == "yes"){
            binding.recycleLayout.visibility = View.VISIBLE
            binding.chatEditLayout.visibility = View.VISIBLE
        } else {
            binding.recycleLayout.visibility = View.GONE
            binding.chatEditLayout.visibility = View.GONE
        }

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)

        var userGender = ""

        Thread{
            database?.userProfileDao()?.getAll()?.forEach {
                when (it.gender.toString()) {
                    m -> m.replace("m", "남자")
                    f -> m.replace("F", "여자")
                    else -> "null"
                }.apply {
                    userGender = this
                }
            }
        }.start()

        val userBirthYear = database?.userProfileDao()?.getAll()?.forEach {
            it.birthday
        }
        val userAge: Int = 2023 - userBirthYear.toString().toInt()

        binding.run {

            val titles = titleHash["title"].toString()
            val categorys = categoryHash["category"].toString()
            val onces = onceHash["once"].toString()
            val locals = localHash["local"].toString()
            val lines = lineHash["line"].toString()
            val descriptions = descriptionHash["description"].toString()
            val nownums = nownumHash["nownum"].toString()
            val peoplenums = peoplenumHash["peoplenum"].toString()
            val genders = genderHash["gender"].toString()
            val minages = minageHash["minage"].toString()
            val maxages = maxageHash["maxage"].toString()
            val images = titleImageHash["image"].toString()

            numberMember.text = "$nownums/$peoplenums"
            title.text = titles
            category.text = categorys
            local.text = locals
            line.text = lines
            once.text = onces
            gender.text = genders
            if (minages == "상관없음") {
                age.text = "상관없음"
            } else {
                age.text = minages + "세" + " ~ " + maxages + "세"
            }
            description.text = descriptions
            Glide.with(requireContext()).load(images).fitCenter().error(R.color.orange)
                .into(groupTitleImg)

            participateBtn.setOnClickListener {
                if (checkUserInfo(userGender, userAge)) {
                    recycleLayout.visibility = View.VISIBLE
                    SharedPreferenceManager.putString(requireContext(), "isParticipate", "yes")
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SportsMapGroupFragment())
                    .commit()

                val mainActivity = activity as MainActivity
                mainActivity.mapGroupSelected()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun checkUserInfo(gender: String, age: Int): Boolean {
        val genders = genderHash["gender"].toString()
        val minages = minageHash["minage"].toString()
        val maxages = maxageHash["maxage"].toString()
        return if (gender == genders && minages == "상관없음") {
            true
        } else if (minages != "상관없음" && age < maxages.toInt() && minages.toInt() < age) {
            true
        } else {
            false
        }
    }
}