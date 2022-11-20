package com.example.sportscommunity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sportscommunity.databinding.WriteContentLayoutBinding

class WriteContentFragment : Fragment() {

    private var mBinding: WriteContentLayoutBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WriteContentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = (activity as MainActivity)
        binding.run {

            selectGroupBtn.setOnClickListener {
                mainActivity.changeWriteFragment(0)
                setColor(
                    selectGroupBtn,
                    selectAloneBtn,
                    selectCommunityBtn,
                    R.drawable.login_btn,
                    Color.WHITE,
                    R.color.orange,
                    R.drawable.wirte_content_btn_background
                )
            }

            selectAloneBtn.setOnClickListener {
                mainActivity.changeWriteFragment(1)
                setColor(
                    selectAloneBtn,
                    selectGroupBtn,
                    selectCommunityBtn,
                    R.drawable.login_btn,
                    Color.WHITE,
                    R.color.orange,
                    R.drawable.wirte_content_btn_background
                )
            }

            selectCommunityBtn.setOnClickListener {
                mainActivity.changeWriteFragment(2)
                setColor(
                    selectCommunityBtn,
                    selectAloneBtn,
                    selectGroupBtn,
                    R.drawable.login_btn,
                    Color.WHITE,
                    R.color.orange,
                    R.drawable.wirte_content_btn_background
                )
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun setColor(
        selectBtn: Button,
        button: Button,
        anotherBtn: Button,
        selectColor: Int,
        selectTextColor: Int,
        textColor: Int,
        color: Int
    ) {

        selectBtn.setTextColor(selectTextColor)
        selectBtn.setBackgroundResource(selectColor)

        button.setTextColor(textColor)
        button.setBackgroundResource(color)

        anotherBtn.setTextColor(textColor)
        anotherBtn.setBackgroundResource(color)
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