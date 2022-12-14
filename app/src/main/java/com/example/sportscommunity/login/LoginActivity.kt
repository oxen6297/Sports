package com.example.sportscommunity.login

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportscommunity.*
import com.example.sportscommunity.repository.Repository
import com.example.sportscommunity.viewmodel.LoginAndSignViewModel
import com.example.sportscommunity.viewmodelfactory.LoginAndSignViewModelFactory
import com.example.sportscommunity.databinding.ActivityLoginBinding
import com.example.sportscommunity.sharedpreference.SharedPreferenceManager
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
import org.json.JSONObject
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
    private var age: String = ""
    private val loginViewModel: LoginAndSignViewModel by viewModels {
        LoginAndSignViewModelFactory(
            Repository()
        )
    }

    private var database: UserDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = UserDatabase.getInstance(this@LoginActivity)

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
                try {
                    naverLogin(this@LoginActivity)
                } catch (e: Exception) {
                    Log.d("postUserError", e.toString())
                }
            }

            loginAnother.setOnClickListener {
                startActivity(Intent(this@LoginActivity, AnotherLoginActivity::class.java))
            }
        }
    }

    //????????? ?????????
    @SuppressLint("ApplySharedPref")
    private fun kakaoLogin(context: Context) {

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "????????????????????? ????????? ??????", error)
            } else if (token != null) {
                Log.i(TAG, "????????????????????? ????????? ?????? ${token.accessToken}")
            }
        }

//         ??????????????? ???????????? ????????? ?????????????????? ?????????, ????????? ????????????????????? ?????????
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("majjem", "?????????????????? ????????? ??????", error)

                    // ???????????? ???????????? ?????? ??? ???????????? ?????? ?????? ???????????? ???????????? ????????? ??????,
                    // ???????????? ????????? ????????? ?????? ????????????????????? ????????? ?????? ?????? ????????? ????????? ?????? (???: ?????? ??????)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // ??????????????? ????????? ?????????????????? ?????? ??????, ????????????????????? ????????? ??????
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i("majjem", "?????????????????? ????????? ?????? ${token.accessToken}")

                    startActivity(Intent(context, MainActivity::class.java))

                    Toast.makeText(context, "????????? ??????", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "????????? ?????? ?????? ??????", error)
            } else if (user != null) {
                val scopes = mutableListOf<String>()

                if (user.kakaoAccount?.emailNeedsAgreement == true) {
                    scopes.add("account_email")
                }
                if (user.kakaoAccount?.birthdayNeedsAgreement == true) {
                    scopes.add("birthday")
                }
                if (user.kakaoAccount?.birthyearNeedsAgreement == true) {
                    scopes.add("birthyear")
                }
                if (user.kakaoAccount?.genderNeedsAgreement == true) {
                    scopes.add("gender")
                }
                if (user.kakaoAccount?.phoneNumberNeedsAgreement == true) {
                    scopes.add("phone_number")
                }
                if (user.kakaoAccount?.profileNeedsAgreement == true) {
                    scopes.add("profile")
                }
                if (user.kakaoAccount?.ageRangeNeedsAgreement == true) {
                    scopes.add("age_range")
                }
                if (user.kakaoAccount?.ciNeedsAgreement == true) {
                    scopes.add("account_ci")
                }

                if (scopes.isNotEmpty()) {
                    Log.d(TAG, "??????????????? ?????? ????????? ????????? ?????????.")

                    // OpenID Connect ?????? ???
                    // scope ????????? "openid" ???????????? ???????????? ???????????? ???
                    // ?????? ???????????? ???????????? ?????? ??????, ID ????????? ??????????????? ??????
                    // scopes.add("openid")

                    //scope ????????? ???????????? ????????? ????????? ??????
                    UserApiClient.instance.loginWithNewScopes(context, scopes) { token, error ->
                        if (error != null) {
                            Log.e("majjem", "????????? ?????? ?????? ??????", error)
                        } else {
                            Log.d("majjem", "allowed scopes: ${token!!.scopes}")

                            // ????????? ?????? ?????????
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e("majjem", "????????? ?????? ?????? ??????", error)
                                } else if (user != null) {
                                    Log.i("majjem", "????????? ?????? ?????? ??????")
                                }
                            }
                        }
                    }
                }
            }
        }

        // ????????? ?????? ?????? (??????)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "????????? ?????? ?????? ??????", error)
            } else if (user != null) {
                Log.e(
                    TAG, "????????? ????????? ?????? ?????? ??????" +
                            "\n????????????: ${user.id}" +
                            "\n?????????: ${user.kakaoAccount?.email}" +
                            "\n?????????: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n???????????????: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +
                            "\n??????: ${user.kakaoAccount?.name}" +
                            "\n??????: ${user.kakaoAccount?.gender}" +
                            "\n????????????: ${user.kakaoAccount?.birthyear}"
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
                Log.d("currentDate", formatted.toString())

                val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
                val formattedTime = currentDate.format(formatterTwo)
                Log.d("currentTime", formattedTime.toString())

                dateTime = "$formatted $formattedTime"
                Log.d("currentDateTime", dateTime)

                /**
                 * ????????? ????????? ?????? ?????? ?????? --------------------------------------------------------
                 */
                val users = HashMap<String, Any>()

                users["username"] = name
                users["nickname"] = nickname
                users["email"] = email
                users["password"] = "password"
                users["create_date"] = dateTime
                users["mobile"] = mobile
                users["modify_date"] = ""
                users["birth"] = birth
                users["image"] = profileImage
                users["gender"] = gender

                Log.d("nicknameOne", nickname)

                loginViewModel.signOrLogin.observe(this@LoginActivity) {
                    if (it.isSuccessful) {
                        val userIdInfo = JSONObject(it.body().toString())
                        val idArray = userIdInfo.optJSONArray("userid")
                        if (idArray != null) {
                            val jsonObject = idArray.getJSONObject(0)
                            val userId = jsonObject.getString("id")
                            SharedPreferenceManager.putInt(context,"id",userId.toString().toInt())
                            SharedPreferenceManager.putString(context,"nickname",nickname)

                            val userProfile = UserProfile(1, email, name,birth,nickname,mobile,gender)

                            Thread {
                                database?.userProfileDao()?.insert(userProfile)
                            }.start()
                        }
                    }
                }
                /**
                 *  ???????????? ------------------------------------------------------------
                 */
                loginViewModel.postSignOrLogin(users)
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

    //????????? ?????????
    private fun naverLogin(context: Context) {

        val oAuthLoginCallback = object : OAuthLoginCallback {

            override fun onError(errorCode: Int, message: String) {
                val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                Log.e("onError", "naverAccessToken : $naverAccessToken")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.d("naverLoginFalied", httpStatus.toString())
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

                    @SuppressLint("ApplySharedPref")
                    override fun onSuccess(result: NidProfileResponse) {
                        name = result.profile?.name.toString()
                        email = result.profile?.email.toString()
                        gender = result.profile?.gender.toString()
                        birth = result.profile?.birthYear.toString()
                        profileImage = result.profile?.profileImage.toString()
                        nickname = result.profile?.nickname.toString()
                        token = result.profile?.id.toString()
                        mobile = result.profile?.mobile.toString()
                        age = result.profile?.age.toString()

                        Log.e("userName", "????????? ???????????? ?????? ?????? - ?????? : $name")
                        Log.e("userEmail", "????????? ???????????? ?????? ?????? - ????????? : $email")
                        Log.e("userGender", "????????? ???????????? ?????? ?????? - ?????? : $gender")
                        Log.e("userBirth", "????????? ???????????? ?????? ?????? - ???????????? : $birth")
                        Log.e("profileImage", "????????? ???????????? ?????? ?????? - ?????? : $profileImage")
                        Log.e("nickname", "????????? ???????????? ?????? ?????? - ??????: $nickname")
                        Log.e("token", "????????? ???????????? ?????? ?????? - ?????? : $token")

                        val currentDate = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ISO_DATE
                        val formatted = currentDate.format(formatter)
                        Log.d("currentDate", formatted.toString())

                        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
                        val formattedTime = currentDate.format(formatterTwo)
                        Log.d("currentTime", formattedTime.toString())

                        dateTime = "$formatted $formattedTime"
                        Log.d("currentDateTime", dateTime)

                        /**
                         * ????????? ????????? ?????? ?????? ?????? --------------------------------------------------------
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
                        user["userimage"] = profileImage
                        user["gender"] = gender
                        user["bestcategory"] = ""
                        user["shortinfo"] = ""

                        loginViewModel.signOrLogin.observe(this@LoginActivity) {

                            if (it.isSuccessful) {
                                val userIdInfo = JSONObject(it.body().toString())
                                val idArray = userIdInfo.optJSONArray("userid")
                                if (idArray != null) {
                                    val jsonObject = idArray.getJSONObject(0)
                                    val userId = jsonObject.getString("id")
                                    SharedPreferenceManager.putInt(context,"id",userId.toString().toInt())
                                    SharedPreferenceManager.putString(context,"nickname",nickname)
//                                    val sharedPreferences =
//                                        getSharedPreferences("userId", Context.MODE_PRIVATE)
//                                    val editor = sharedPreferences.edit()
//                                    editor.putInt("id", userId.toString().toInt())
//                                    editor.commit()
                                }
                            }
                        }
                        loginViewModel.postSignOrLogin(user)
                        /**
                         *  ???????????? ------------------------------------------------------------
                         */

                        val userProfile = UserProfile(1, email, name,birth,nickname,mobile,gender)

                        Thread {
                            database?.userProfileDao()?.insert(userProfile)
                        }.start()

                        startActivity(Intent(context, MainActivity::class.java))
                        Toast.makeText(context, "????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        NaverIdLoginSDK.initialize(
            this,
            getString(R.string.naver_cliend_id),
            getString(R.string.naver_cliend_secret),
            "??????"
        )
        NaverIdLoginSDK.authenticate(this, oAuthLoginCallback)
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