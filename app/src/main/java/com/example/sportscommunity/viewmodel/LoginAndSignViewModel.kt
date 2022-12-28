package com.example.sportscommunity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportscommunity.Repository.Repository
import com.example.sportscommunity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginAndSignViewModel(private val repository: Repository) : ViewModel() {
    private val _signOrLogin: MutableLiveData<Response<String>> = MutableLiveData()
    val signOrLogin: LiveData<Response<String>>
        get() = _signOrLogin

    private val _putInfo: MutableLiveData<Response<String>> = MutableLiveData()
    val putInfo: LiveData<Response<String>>
        get() = _putInfo

    var inputNickname: MutableLiveData<String> = MutableLiveData()

    var inputLikeCategory: MutableLiveData<String> = MutableLiveData()

    var inputShortInfo: MutableLiveData<String> = MutableLiveData()

    var userProfileImage: MutableLiveData<String> = MutableLiveData()


    fun postSignOrLogin(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.loginAndSignUp(hashMap)
                _signOrLogin.value = response
            } catch (e: Exception) {
                Log.d("postUserError", e.toString())
            }
        }
    }

    fun putUserInfo(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    val response = repository.putUserInfo(hashMap)
                    _putInfo.value = response
                } catch (e: Exception) {
                    Log.d("putUserError", e.toString())
                }
            }
        }
    }

    fun saveBtnClick(){
        val postUser = HashMap<String, Any>()
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)

        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)

        val writeTime = "$formatted $formattedTime"
        postUser["nickname"] = inputNickname.value.toString()
        postUser["userimage"] = userProfileImage.value.toString()
        postUser["bestcategory"] = "inputLikeCategory.value.toString()"
        postUser["shortinfo"] = inputShortInfo.value.toString()
        postUser["modify_date"] = writeTime
        postUser["id"] = 180
        putUserInfo(postUser)
    }
}