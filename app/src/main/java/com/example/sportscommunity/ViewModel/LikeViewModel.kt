package com.example.sportscommunity.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportscommunity.Repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class LikeViewModel(private val repository: Repository) : ViewModel() {
    private val _comLike: MutableLiveData<Response<String>> = MutableLiveData()
    private val _postComLike: MutableLiveData<Response<String>> = MutableLiveData()


    val getComLike: LiveData<Response<String>>
        get() = _comLike
    val postComLike: LiveData<Response<String>>
        get() = _postComLike

    fun getComLike(hashMap: HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val response = repository.postComLikeCount(hashMap)
                _comLike.value = response
            }catch (e:Exception){
                Log.d("getLikeError",e.toString())
            }
        }
    }

    fun postComLike(hashMap: HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val response = repository.postComLike(hashMap)
                _postComLike.value = response
            }catch (e:Exception){
                Log.d("getLikeError",e.toString())
            }
        }
    }
}