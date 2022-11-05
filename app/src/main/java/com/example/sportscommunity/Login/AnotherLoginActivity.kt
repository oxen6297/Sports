package com.example.sportscommunity.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.R
import com.example.sportscommunity.databinding.ActivityAnotherLoginBinding
import com.example.sportscommunity.databinding.ActivitySignUpBinding

class AnotherLoginActivity : AppCompatActivity() {

    private var mBinding: ActivityAnotherLoginBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAnotherLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.hide()

        binding.run {
            backBtn.setOnClickListener {
                onBackPressed()
            }

            signupBtn.setOnClickListener {
                startActivity(Intent(this@AnotherLoginActivity, SignUpActivity::class.java))
            }

            findPassword.setOnClickListener {
                startActivity(Intent(this@AnotherLoginActivity, FindPasswordActivity::class.java))
            }

            findId.setOnClickListener {
                startActivity(Intent(this@AnotherLoginActivity, FindIdActivity::class.java))
            }

            loginBtn.setOnClickListener {

                if (checkInfo(loginEmailEdit, "아이디") && checkInfo(loginPasswordEdit, "패스워호")) {

                    Toast.makeText(this@AnotherLoginActivity, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AnotherLoginActivity, MainActivity::class.java))
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