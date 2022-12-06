package com.example.sportscommunity.Login

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportscommunity.*
import com.example.sportscommunity.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!
    private var email: String = ""
    private var gender: String = ""
    private var name: String = ""
    private var birth: String = ""
    private var profileImage: String = ""
    private var nickname: String = ""
    private var token: String = ""
    private var mobile: String = ""
    private var dateTime: String = ""
    private val naverType: String = "naver"
    private val kakaoType: String = "kakao"

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
                startActivity(Intent(this@LoginActivity, AnotherLoginActivity::class.java))
            }


        }
    }

    //카카오 로그인
    private fun kakaoLogin(context: Context) {

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

//         카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("majjem", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i("majjem", "카카오톡으로 로그인 성공 ${token.accessToken}")

                    startActivity(Intent(context, MainActivity::class.java))

                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                val scopes = mutableListOf<String>()

                if (user.kakaoAccount?.emailNeedsAgreement == true) { scopes.add("account_email") }
                if (user.kakaoAccount?.birthdayNeedsAgreement == true) { scopes.add("birthday") }
                if (user.kakaoAccount?.birthyearNeedsAgreement == true) { scopes.add("birthyear") }
                if (user.kakaoAccount?.genderNeedsAgreement == true) { scopes.add("gender") }
                if (user.kakaoAccount?.phoneNumberNeedsAgreement == true) { scopes.add("phone_number") }
                if (user.kakaoAccount?.profileNeedsAgreement == true) { scopes.add("profile") }
                if (user.kakaoAccount?.ageRangeNeedsAgreement == true) { scopes.add("age_range") }
                if (user.kakaoAccount?.ciNeedsAgreement == true) { scopes.add("account_ci") }

                if (scopes.count() > 0) {
                    Log.d(TAG, "사용자에게 추가 동의를 받아야 합니다.")

                    // OpenID Connect 사용 시
                    // scope 목록에 "openid" 문자열을 추가하고 요청해야 함
                    // 해당 문자열을 포함하지 않은 경우, ID 토큰이 재발급되지 않음
                    // scopes.add("openid")

                    //scope 목록을 전달하여 카카오 로그인 요청
                    UserApiClient.instance.loginWithNewScopes(context, scopes) { token, error ->
                        if (error != null) {
                            Log.e("majjem", "사용자 추가 동의 실패", error)
                        } else {
                            Log.d("majjem", "allowed scopes: ${token!!.scopes}")

                            // 사용자 정보 재요청
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e("majjem", "사용자 정보 요청 실패", error)
                                }
                                else if (user != null) {
                                    Log.i("majjem", "사용자 정보 요청 성공")
                                }
                            }
                        }
                    }
                }
            }
        }

        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.e(
                    TAG, "카카오 사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +
                            "\n이름: ${user.kakaoAccount?.name}" +
                            "\n성별: ${user.kakaoAccount?.gender}" +
                            "\n생년월일: ${user.kakaoAccount?.birthyear}"
                )

                name = user.kakaoAccount?.name.toString()
                email = user.kakaoAccount?.email.toString()
                gender = user.kakaoAccount?.gender.toString()
                birth = user.kakaoAccount?.birthyear.toString()
                profileImage = user.kakaoAccount?.profile?.thumbnailImageUrl.toString()
                nickname = user.kakaoAccount?.profile?.nickname.toString()
                token = user.id.toString()
                mobile = user.kakaoAccount?.phoneNumber.toString()

                val currentDate = LocalDateTime.now()
                val formatter = DateTimeFormatter.ISO_DATE
                val formatted = currentDate.format(formatter)
                Log.d("currentDate",formatted.toString())

                val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
                val formattedTime = currentDate.format(formatterTwo)
                Log.d("currentTime",formattedTime.toString())

                dateTime = "$formatted $formattedTime"
                Log.d("currentDateTime",dateTime)

                /**
                 * 카카오 로그인 서버 연동 부분 --------------------------------------------------------
                 */
                val user = HashMap<String,Any>()

                user["username"] = name
                user["nickname"] = nickname
                user["email"] = email
                user["password"] = "password"
                user["create_date"] = dateTime
                user["mobile"] = mobile
                user["modify_date"] = ""
                user["birth"] = birth
                user["image"] = profileImage
                user["gender"] = gender

                val retrofitService = Retrofits.postUserInfo()
                val call: Call<User> = retrofitService.postUser(user)

                call.enqueue(object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        try {
                            if (response.isSuccessful) {
                                Log.e("userInfoPost", "success")
                                Log.d("성공:", "${response.body()}")
                            }
                        } catch (e: Exception) {
                            Log.e("userInfoPost", response.body().toString())
                            Log.e("userInfoPost", response.message().toString())
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e("userInfoPost", t.message.toString())
                    }
                })
                /**
                 *  여기까지 ------------------------------------------------------------
                 */
            }
        }
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
//            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(
//                context,
//                callback = callback
//            )
//        }
    }

    //네이버 로그인
    private fun naverLogin(context: Context) {

        val database = Room.databaseBuilder(
            applicationContext, UserDatabase::class.java,
            "user-database"
        ).build()

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
                        birth = result.profile?.birthYear.toString()
                        profileImage = result.profile?.profileImage.toString()
                        nickname = result.profile?.nickname.toString()
                        token = result.profile?.id.toString()
                        mobile = result.profile?.mobile.toString()

                        Log.e("userName", "네이버 로그인한 유저 정보 - 이름 : $name")
                        Log.e("userEmail", "네이버 로그인한 유저 정보 - 이메일 : $email")
                        Log.e("userGender", "네이버 로그인한 유저 정보 - 성별 : $gender")
                        Log.e("userBirth", "네이버 로그인한 유저 정보 - 생년월일 : $birth")
                        Log.e("profileImage", "네이버 로그인한 유저 정보 - 프사 : $profileImage")
                        Log.e("nickname", "네이버 로그인한 유저 정보 - 별명: $nickname")
                        Log.e("token", "네이버 로그인한 유저 정보 - 토큰 : $token")

                        val currentDate = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ISO_DATE
                        val formatted = currentDate.format(formatter)
                        Log.d("currentDate",formatted.toString())

                        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
                        val formattedTime = currentDate.format(formatterTwo)
                        Log.d("currentTime",formattedTime.toString())

                        dateTime = "$formatted $formattedTime"
                        Log.d("currentDateTime",dateTime)

                        /**
                         * 네이버 로그인 서버 연동 부분 --------------------------------------------------------
                         */
                        val user = HashMap<String, Any>()
                        user["username"] = name
                        user["nickname"] = nickname
                        user["email"] = email
                        user["password"] = "password"
                        user["create_date"] = dateTime
                        user["mobile"] = mobile
                        user["modify_date"] = ""
                        user["birth"] = birth
                        user["image"] = profileImage
                        user["gender"] = gender

                        val retrofitService = Retrofits.postUserInfo()
                        val call: Call<User> = retrofitService.postUser(user)

                        call.enqueue(object : Callback<User> {
                            override fun onResponse(
                                call: Call<User>,
                                response: Response<User>
                            ) {
                                try {
                                    if (response.isSuccessful) {
                                        Log.e("userInfoPost", "success")
                                        Log.d("성공:", "${response.body()}")
                                    }
                                } catch (e: Exception) {
                                    Log.e("userInfoPost", response.body().toString())
                                    Log.e("userInfoPost", response.message().toString())
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.e("userInfoPost", t.message.toString())
                            }
                        })
                        /**
                         *  여기까지 ------------------------------------------------------------
                         */

                        val userProfile = UserProfile(1, email, name)

                        Thread(Runnable {
                            val userProfiles = database.userProfileDao().getAll()

                            database.userProfileDao().insert(userProfile)

                            userProfiles.forEach {
                                Log.d("roomDatabase", "" + it.email)
                            }
                        }).start()



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