package com.example.sportscommunity

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sportscommunity.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!

    private var email: String = ""
    private var gender: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.findPassword.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.signUpBtn.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        val keyHash = Utility.getKeyHash(this)
        Log.d("keyHash", keyHash)

        val actionBar = supportActionBar
        actionBar?.hide()

        binding.findPassword.setOnClickListener {
            startActivity(Intent(this, FindPasswordActivity::class.java))
        }

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.kakaoLoginBtn.setOnClickListener {

//            // 로그인 정보 확인 (토큰 값이 있으면 자동 로그인)
//            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//                if (error != null) {
//                    Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
//                }
//                else if (tokenInfo != null) {
//                    Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                }
//            }

            // 카카오계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Log.d("tokenError", "로그인 실패", error)
                } else if (token != null) {
                    Log.d("LoginSuccess", "로그인 성공 ${token.accessToken}")
                    val startMainIntent = Intent(this, MainActivity::class.java)
                    startActivity(startMainIntent)
                }
            }

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.d("로그인 실패", error.toString())
                } else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    Log.d("로그인성공", token.idToken.toString())
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    this@LoginActivity,
                    callback = callback
                )
            }
        }

        binding.run {
            //네이버 API 연동
            naverLoginBtn.setOnClickListener {

                val oAuthLoginCallback = object : OAuthLoginCallback {

                    override fun onError(errorCode: Int, message: String) {
                        val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                        Log.e("onError", "naverAccessToken : $naverAccessToken")
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                    }

                    override fun onSuccess() {

                        NidOAuthLogin().callProfileApi(object :
                            NidProfileCallback<NidProfileResponse> {
                            override fun onError(errorCode: Int, message: String) {
                                Log.d("NaverLoginError", message)
                            }

                            override fun onFailure(httpStatus: Int, message: String) {
                                Log.d("NaverLoginError", "$httpStatus\n" + message)
                            }

                            override fun onSuccess(result: NidProfileResponse) {
                                name = result.profile?.name.toString()
                                email = result.profile?.email.toString()
                                gender = result.profile?.gender.toString()
                                Log.e("userName", "네이버 로그인한 유저 정보 - 이름 : $name")
                                Log.e("userEmail", "네이버 로그인한 유저 정보 - 이메일 : $email")
                                Log.e("userGender", "네이버 로그인한 유저 정보 - 성별 : $gender")

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            }
                        })
                    }
                }

                NaverIdLoginSDK.initialize(
                    this@LoginActivity,
                    getString(R.string.naver_cliend_id),
                    getString(R.string.naver_cliend_secret),
                    "맺음"
                )
                NaverIdLoginSDK.authenticate(this@LoginActivity, oAuthLoginCallback)
            }
        }
    }
}