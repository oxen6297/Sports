package com.example.sportscommunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.SportsCommunityFragmentBinding
import com.example.sportscommunity.databinding.SportsHomeFragmentBinding

class SportsCommunityFragment : Fragment() {

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

        binding.foldingLayout.setOnClickListener {
            if (binding.detailLayout.visibility == View.GONE) {
                binding.detailLayout.visibility = View.VISIBLE
                binding.expandBtn.animate().apply {
                    duration = 300
                    rotation(180f)
                }
            } else {
                binding.detailLayout.visibility = View.GONE
                binding.expandBtn.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}