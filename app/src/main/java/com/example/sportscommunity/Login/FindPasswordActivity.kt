package com.example.sportscommunity.Login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.sportscommunity.databinding.ActivityFindPasswordBinding

class FindPasswordActivity : AppCompatActivity() {

    private var mBinding: ActivityFindPasswordBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.hide()

        binding.run {

            backBtn.setOnClickListener {
                onBackPressed()
            }

            checkNull(findEmailEdit, findCheckEmailText, "이메일을")
            checkNull(findSmsEdit, findCheckSmsText, "인증번호를")

            checkPasswordTextView(findJoinPasswordEdit, findConfirmPassword, findCheckPassword)

            goSignInBtn.setOnClickListener {
                if (checkInfo(findEmailEdit, "아이디") && checkInfo(
                        findJoinPasswordEdit,
                        "비밀번호"
                    ) && checkInfo(findConfirmPassword, "비밀번호 확인")
                ) {
                    startActivity(
                        Intent(
                            this@FindPasswordActivity,
                            AnotherLoginActivity::class.java
                        )
                    )
                }


            }
        }
    }

    //이메일, 인증번호 빈칸 확인 텍스트 출력
    @SuppressLint("SetTextI18n")
    private fun checkNull(editText: EditText, textView: TextView, content: String) {

        editText.doAfterTextChanged {
            if (editText.text.isEmpty()) {
                textView.text = "$content 입력해주세요."
            }
        }
    }

    //비밀번호 입력칸에 따라 텍스트 메세지 변경
    private fun checkPasswordTextView(
        password: EditText,
        confirmPassword: EditText,
        passwordText: TextView
    ) {
        confirmPassword.doAfterTextChanged {

            if (password.text.toString() != confirmPassword.text.toString()) {
                passwordText.text = "비밀번호가 일치하지 않습니다."

            } else if (password.text.toString().isEmpty() || confirmPassword.text.toString()
                    .isEmpty()
            ) {
                passwordText.text = "비밀번호를 입력해주세요."

            } else {
                passwordText.text = "비밀번호가 일치합니다."
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