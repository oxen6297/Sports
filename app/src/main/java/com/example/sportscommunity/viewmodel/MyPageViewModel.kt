package com.example.sportscommunity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportscommunity.MyWrite
import com.example.sportscommunity.repository.Repository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MyPageViewModel(private val repository: Repository) : ViewModel() {

    private val _myWriteResponse: MutableLiveData<Response<JSONObject>> = MutableLiveData()
    private val _myLikeResponse: MutableLiveData<Response<JSONObject>> = MutableLiveData()

    val myWriteResponse: LiveData<Response<JSONObject>>
        get() = _myWriteResponse

    val myLikeResponse: LiveData<Response<JSONObject>>
        get() = _myLikeResponse

    fun postMyWriteInfo(hashMap: HashMap<String,Any>) {
        viewModelScope.launch {
            try {
                val response = repository.postMyWrite(hashMap)
                _myWriteResponse.value = response
            }catch (e:Exception){
                Log.d("postMyWriteError",e.toString())
            }
        }
    }

    fun postMyLikeInfo(hashMap: HashMap<String,Any>) {
        viewModelScope.launch {
            try {
                val response = repository.postMyLike(hashMap)
                _myLikeResponse.value = response
            }catch (e:Exception){
                Log.d("postMyLikeError",e.toString())
            }
        }
    }
}