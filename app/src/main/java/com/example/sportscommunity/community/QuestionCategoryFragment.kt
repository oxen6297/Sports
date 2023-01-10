package com.example.sportscommunity.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sportscommunity.adapter.FreeBoardAdapter
import com.example.sportscommunity.Content
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.repository.Repository
import com.example.sportscommunity.viewmodel.MainViewModel
import com.example.sportscommunity.viewmodelfactory.MainViewModelFactory
import com.example.sportscommunity.WriteContentFragment
import com.example.sportscommunity.databinding.QuestionCategoryTabBinding
import com.example.sportscommunity.writeFlag

class QuestionCategoryFragment: Fragment() {

    private var mBinding: QuestionCategoryTabBinding? = null
    private val binding get() = mBinding!!
    private val mainViewModel: MainViewModel by viewModels {MainViewModelFactory(Repository())}
    private lateinit var freeBoardAdapter: FreeBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = QuestionCategoryTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        getCom(mainActivity)
        mainActivity.hideBottomNavigationView(true)

        binding.write.setOnClickListener {
            mainActivity.changeFragment(0)
            mainActivity.setDataAtFragment(WriteContentFragment(),1 ,"write")
            writeFlag.put("write",9)
        }

        binding.searchEdit.doAfterTextChanged {
            freeBoardAdapter.filter.filter(it.toString())
        }
    }

    private fun getCom(mainActivity: MainActivity){
        mainViewModel.communityResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                freeBoardAdapter = FreeBoardAdapter(it.body()?.boardwrite9, mainActivity)
                binding.questionBoardRecycle.adapter = freeBoardAdapter

            } else {
                Log.d("comError", it.errorBody().toString())
            }
        }
        mainViewModel.getCommunity("9")
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
}