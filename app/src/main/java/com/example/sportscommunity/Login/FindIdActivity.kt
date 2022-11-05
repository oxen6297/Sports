package com.example.sportscommunity.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.ActivityFindIdBinding
import com.example.sportscommunity.databinding.ActivitySignUpBinding

class FindIdActivity : AppCompatActivity() {

    private var mBinding: ActivityFindIdBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.hide()

        binding.run {
            backBtn.setOnClickListener {
                onBackPressed()
            }

            goLoginBtn.setOnClickListener {
                if (checkInfo(findIdEdit,"아이디")){
                    startActivity(Intent(this@FindIdActivity,AnotherLoginActivity::class.java))
                }

            }
        }
    }

    // 빈칸 확인
    private fun checkInfo(editText: EditText, name: String): Boolean {
        return if (editText.text.isNullOrBlank()) {
            Toast.makeText(this, "$name 칸은 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
}