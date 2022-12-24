package com.example.sportscommunity.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.sportscommunity.Adapter.FreeBoardAdapter
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.Repository.Repository
import com.example.sportscommunity.ViewModel.MainViewModel
import com.example.sportscommunity.ViewModelFactory.MainViewModelFactory
import com.example.sportscommunity.WriteContentFragment
import com.example.sportscommunity.databinding.FreeCategoryTabBinding
import com.example.sportscommunity.writeFlag

class FreeCategoryFragment : Fragment() {

    private var mBinding: FreeCategoryTabBinding? = null
    private val binding get() = mBinding!!
    private val mainViewModel: MainViewModel by viewModels {MainViewModelFactory(Repository())}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FreeCategoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        getCom(mainActivity)

        mainActivity.hideBottomNavigationView(true)

        binding.write.setOnClickListener {
            mainActivity.changeFragment(0)
            mainActivity.setDataAtFragment(WriteContentFragment(), 1, "write")
            writeFlag.put("write", 7)
        }
    }

    private fun getCom(mainActivity: MainActivity){
        mainViewModel.communityResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val freeBoardAdapter = FreeBoardAdapter(it.body()?.boardwrite7, mainActivity)
                freeBoardAdapter.setHasStableIds(true)
                binding.freeBoardRecycle.adapter = freeBoardAdapter

            } else {
                Log.d("comError", it.errorBody().toString())
            }
        }
        mainViewModel.getCommunity("7")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}
