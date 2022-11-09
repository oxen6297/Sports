package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.databinding.SportsCommunityFragmentBinding

class SportsCommunityFragment : Fragment(), View.OnClickListener {

    private var mBinding: SportsCommunityFragmentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SportsCommunityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)

        mainActivity.hideBottomNavigationView(false)

        binding.run {

            clickListener()

            foldingLayout.setOnClickListener {
                if (detailLayout.visibility == View.GONE) {
                    detailLayout.visibility = View.VISIBLE
                    expandBtn.animate().apply {
                        duration = 300
                        rotation(180f)
                    }
                } else {
                    detailLayout.visibility = View.GONE
                    expandBtn.animate().apply {
                        duration = 300
                        rotation(0f)
                    }
                }
            }
        }
    }

    private fun clickListener() {
        binding.run {
            freeImg.setOnClickListener(this@SportsCommunityFragment)
            freeBoardText.setOnClickListener(this@SportsCommunityFragment)
            secretBoardText.setOnClickListener(this@SportsCommunityFragment)
            secretImg.setOnClickListener(this@SportsCommunityFragment)
            questionImg.setOnClickListener(this@SportsCommunityFragment)
            questionText.setOnClickListener(this@SportsCommunityFragment)
            faqImg.setOnClickListener(this@SportsCommunityFragment)
            faqText.setOnClickListener(this@SportsCommunityFragment)
            ballImg.setOnClickListener(this@SportsCommunityFragment)
            ballText.setOnClickListener(this@SportsCommunityFragment)
            leisureText.setOnClickListener(this@SportsCommunityFragment)
            leisureImg.setOnClickListener(this@SportsCommunityFragment)
            lifeSportsImg.setOnClickListener(this@SportsCommunityFragment)
            lifeText.setOnClickListener(this@SportsCommunityFragment)
            waveImg.setOnClickListener(this@SportsCommunityFragment)
            waterText.setOnClickListener(this@SportsCommunityFragment)
            winterSportsImg.setOnClickListener(this@SportsCommunityFragment)
            winterText.setOnClickListener(this@SportsCommunityFragment)
            eSportsImg.setOnClickListener(this@SportsCommunityFragment)
            eSportsText.setOnClickListener(this@SportsCommunityFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressed(requireContext(), requireActivity())
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "커뮤니티"
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun onClick(v: View?) {
        val mainActivity = (activity as MainActivity)
        binding.run {
            when (v?.id) {
                ballImg.id -> mainActivity.changeFragment(4)
                ballText.id -> mainActivity.changeFragment(4)
                leisureImg.id -> mainActivity.changeFragment(5)
                leisureText.id -> mainActivity.changeFragment(5)
                waveImg.id -> mainActivity.changeFragment(6)
                waterText.id -> mainActivity.changeFragment(6)
                lifeSportsImg.id -> mainActivity.changeFragment(7)
                lifeText.id -> mainActivity.changeFragment(7)
                winterSportsImg.id -> mainActivity.changeFragment(8)
                winterText.id -> mainActivity.changeFragment(8)
                eSportsImg.id -> mainActivity.changeFragment(9)
                eSportsText.id -> mainActivity.changeFragment(9)
                freeImg.id -> mainActivity.changeFragment(1)
                freeBoardText.id -> mainActivity.changeFragment(1)
                secretImg.id -> mainActivity.changeFragment(10)
                secretBoardText.id -> mainActivity.changeFragment(10)
                questionImg.id -> mainActivity.changeFragment(11)
                questionText.id -> mainActivity.changeFragment(11)
                faqImg.id -> mainActivity.changeFragment(12)
                faqText.id -> mainActivity.changeFragment(12)
            }
        }
    }

}