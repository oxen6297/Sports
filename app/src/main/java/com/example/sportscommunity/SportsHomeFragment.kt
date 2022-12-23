package com.example.sportscommunity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sportscommunity.Adapter.AloneAdapter
import com.example.sportscommunity.Adapter.GroupAdapter
import com.example.sportscommunity.Adapter.backPressed
import com.example.sportscommunity.Repository.Repository
import com.example.sportscommunity.ViewModel.MainViewModel
import com.example.sportscommunity.ViewModelFactory.MainViewModelFactory
import com.example.sportscommunity.databinding.SportsHomeFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsHomeFragment : Fragment() {

    lateinit var binding: SportsHomeFragmentBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.sports_home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        val mainActivity = activity as MainActivity

        mainViewModel = ViewModelProvider(
            this@SportsHomeFragment,
            viewModelFactory
        )[MainViewModel::class.java]

        binding.onclick = this
        mainActivity.hideBottomNavigationView(false)

        mainViewModel.getNews()
        mainViewModel.getGroup()
        mainViewModel.getAlone()
        mainViewModel.getBestBoard()

        mainViewModel.bestBoardResponse.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                it.body()?.bestwrite?.forEach {  best ->
                    binding.run {
                        bestTitle.text = best.title.toString()
                        userNickname.text = best.nickname.toString()
                        userContent.text = best.description.toString()
                        writeDate.text = best.writedate.toString()
                        likeNum.text = best.likedcount.toString()
                        Glide.with(requireContext())
                            .load(best.profileimage.toString())
                            .error(R.color.orange).centerCrop().into(userImg)

                        hotLayout.setOnClickListener {
                            titleHash.put("title", best.title.toString())
                            descriptionHash.put(
                                "description",
                                userContent.text.toString()
                            )
                            userImageHash.put("image", best.profileimage.toString())
                            nicknameHash.put("nickname", best.nickname.toString())
                            writedateHash.put(
                                "writedate",
                                best.writedate.toString()
                            )
                            FreeBoardId.put("boardId", best.boardid.toString())
                            categoryHash.put("categoryId", best.id.toString())

                            mainActivity.changeFragment(19)
                        }
                    }
                }
            } else {
                Log.d("error",it.errorBody().toString())
            }
        }

        mainViewModel.aloneResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val aloneAdapter =
                    AloneAdapter(requireContext(), it.body()?.individualwrite, mainActivity)
                aloneAdapter.setHasStableIds(true)
                binding.aloneRecycle.adapter = aloneAdapter
                binding.aloneRecycle.setHasFixedSize(true)
            } else {
                Log.d("AloneError", it.errorBody().toString())
            }
        }

        mainViewModel.groupResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val groupAdapter =
                    GroupAdapter(requireContext(), it.body()?.groupwrite, mainActivity)
                groupAdapter.setHasStableIds(true)
                binding.groupRecycle.adapter = groupAdapter
                binding.groupRecycle.setHasFixedSize(true)
            } else {
                Log.d("GroupError", it.errorBody().toString())
            }
        }

        mainViewModel.newsResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val newsAdapter = ListSourceAdapter(requireContext(), it.body()?.articles)
                newsAdapter.setHasStableIds(true)
                binding.newsRecycle.apply {
                    this.adapter = newsAdapter
                }
            } else {
                Log.d("NewsError", it.errorBody().toString())
            }
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            mainViewModel.getBestBoard()
            mainViewModel.getNews()
            mainViewModel.getGroup()
            mainViewModel.getAlone()
        }
    }

    fun onClick(v: View?) {
        val mainActivity = (activity as MainActivity)
        binding.run {
            when (v?.id) {
                ballImg.id -> mainActivity.changeFragment(4)
                ballText.id -> mainActivity.changeFragment(4)
                leisureImg.id -> mainActivity.changeFragment(5)
                leisureText.id -> mainActivity.changeFragment(5)
                wave.id -> mainActivity.changeFragment(6)
                waterText.id -> mainActivity.changeFragment(6)
                lifeSports.id -> mainActivity.changeFragment(7)
                lifeText.id -> mainActivity.changeFragment(7)
                winterSports.id -> mainActivity.changeFragment(8)
                winterText.id -> mainActivity.changeFragment(8)
                eSports.id -> mainActivity.changeFragment(9)
                gameText.id -> mainActivity.changeFragment(9)
                groupTitle.id -> mainActivity.changeFragment(13).apply {
                    mainActivity.itemSelected()
                }
                groupRecycle.id -> mainActivity.changeFragment(13).apply {
                    mainActivity.itemSelected()
                }
                goGroupBtn.id -> mainActivity.changeFragment(13).apply {
                    mainActivity.itemSelected()
                }
                aloneTitle.id -> mainActivity.changeFragment(20).apply {
                    mainActivity.itemSelected()
                    mainActivity.setDataAtFragment(SportsMapGroupFragment(), 1, "alone")
                }
                aloneRecycle.id -> mainActivity.changeFragment(20).apply {
                    mainActivity.itemSelected()
                    mainActivity.setDataAtFragment(SportsMapGroupFragment(), 1, "alone")
                }
                goAloneBtn.id -> mainActivity.changeFragment(20).apply {
                    mainActivity.itemSelected()
                    mainActivity.setDataAtFragment(SportsMapGroupFragment(), 1, "alone")
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

        (activity as AppCompatActivity).supportActionBar?.title = "홈"
    }
}