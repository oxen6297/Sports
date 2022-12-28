package com.example.sportscommunity.Login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.Repository.Repository
import com.example.sportscommunity.viewmodel.LoginAndSignViewModel
import com.example.sportscommunity.ViewModelFactory.LoginAndSignViewModelFactory
import com.example.sportscommunity.databinding.ActivitySignUpBinding
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SignUpActivity : AppCompatActivity() {

    private var mBinding: ActivitySignUpBinding? = null
    private val binding get() = mBinding!!
    private var name = ""
    private var nickname = ""
    private var email = ""
    private var password = ""
    private var signTime = ""
    private val signUpViewModel: LoginAndSignViewModel by viewModels {
        LoginAndSignViewModelFactory(
            Repository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.hide()

        binding.run {

            backBtn.setOnClickListener {
                onBackPressed()
            }

            checkNull(nicknameEdit, checkNicknameText, "별명을")
            checkNull(joinEmailEdit, checkIdText, "이메일을")

            checkPasswordTextView(joinPasswordEdit, confirmPassword, checkPasswordText)

            createAccount.setOnClickListener {

                if (checkInfo(nameEdit, "이름") &&
                    checkInfo(nicknameEdit, "별명") &&
//                    checkInfo(phoneNumberEdit, "휴대폰 번호") &&
//                    checkInfo(smsEdit, "인증번호") &&
                    checkInfo(joinEmailEdit, "이메일") &&
                    checkInfo(joinPasswordEdit, "비밀번호") &&
                    checkInfo(confirmPassword, "비밀번호 확인") &&
                    checkConfirmPassword(joinPasswordEdit, confirmPassword)

                ) {
                    signUpRetrofit()
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

    //비밀번호 일치 확인 토스트 메세지
    private fun checkConfirmPassword(
        passwordEdit: EditText,
        confirmPassword: EditText,
    ): Boolean {
        return if (passwordEdit.text.toString() != confirmPassword.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            false

        } else {
            true
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

    //별명, 인증번호, 이메일 비어있으면 입력해달라는 텍스트 출력
    @SuppressLint("SetTextI18n")
    private fun checkNull(editText: EditText, textView: TextView, content: String) {

        editText.doAfterTextChanged {
            if (editText.text.isEmpty()) {
                textView.text = "$content 입력해주세요."
            }
        }
    }

    @SuppressLint("CommitPrefEdits", "ApplySharedPref")
    private fun signUpRetrofit() {
        name = binding.nameEdit.text.toString()
        nickname = binding.nicknameEdit.text.toString()
        email = binding.joinEmailEdit.text.toString()
        password = binding.joinPasswordEdit.text.toString()
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)
        Log.d("currentDate", formatted.toString())

        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)
        Log.d("currentTime", formattedTime.toString())

        signTime = "$formatted $formattedTime"
        Log.d("currentDateTime", signTime)

        val user = HashMap<String, Any>()
        user["username"] = name
        user["nickname"] = nickname
        user["email"] = email
        user["password"] = password
        user["create_date"] = signTime
//        user["mobile"] = "123"
//        user["modify_date"] = "asd"
//        user["birth"] = "asd"
//        user["image"] = "asd"
//        user["gender"] = ""

        signUpViewModel.signOrLogin.observe(this@SignUpActivity){
            if (it.isSuccessful){

                val userIdInfo = JSONObject(it.body().toString())
                val idArray = userIdInfo.optJSONArray("userid")
                if (idArray != null) {
                    val jsonObject = idArray.getJSONObject(0)
                    val userId = jsonObject.getString("id")

                    val sharedPreferences =
                        getSharedPreferences("userId", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.putInt("id", userId.toString().toInt())
                    Log.d("majeumUserId", userId.toString())
                    editor.commit()
                }

                Toast.makeText(this@SignUpActivity, "회원가입 완료", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
            }
        }
        signUpViewModel.postSignOrLogin(user)
    }
}
