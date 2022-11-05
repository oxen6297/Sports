package com.example.sportscommunity.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportscommunity.MainActivity
import com.example.sportscommunity.R
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

        binding.run {

            val keyHash = Utility.getKeyHash(this@LoginActivity)
            Log.d("keyHash", keyHash)

            val actionBar = supportActionBar
            actionBar?.hide()

            vPager.adapter = LoginPagerAdapter(this@LoginActivity, 4)

            dotsIndicator.setViewPager2(vPager)

            kakaoLoginBtn.setOnClickListener {
                kakaoLogin(this@LoginActivity)
            }

            naverLoginBtn.setOnClickListener {
                naverLogin(this@LoginActivity)
            }

            loginAnother.setOnClickListener {
                startActivity(Intent(this@LoginActivity,AnotherLoginActivity::class.java))
            }


        }
    }

    //카카오 로그인
    private fun kakaoLogin(context: Context) {

        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {

                Log.d("failed to Login", "로그인 실패", error)

            } else if (token != null) {

                Log.d("Success Login", "로그인 성공 ${token.accessToken}")

                startActivity(Intent(context, MainActivity::class.java))

                Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
            }
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {

                Log.d("로그인 실패", error.toString())

            } else if (token != null) {
                UserApiClient.instance.me { user, error ->

                    Log.d("로그인성공", token.idToken.toString())
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                context,
                callback = callback
            )
        }
    }

    //네이버 로그인
    private fun naverLogin(context: Context) {

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

                        startActivity(Intent(context, MainActivity::class.java))
                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        NaverIdLoginSDK.initialize(
            context,
            getString(R.string.naver_cliend_id),
            getString(R.string.naver_cliend_secret),
            "맺음"
        )
        NaverIdLoginSDK.authenticate(context, oAuthLoginCallback)
    }

    class LoginPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val tabCount: Int
    ) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return tabCount
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ViewpagerFragmentOne()
                1 -> ViewpagerFragmentTwo()
                2 -> ViewpagerFragmentThree()
                else -> ViewpagerFragmentFour()
            }
        }
    }
}