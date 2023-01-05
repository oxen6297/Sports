package com.example.sportscommunity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportscommunity.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject
constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _getChatResponse: MutableLiveData<Response<String>> = MutableLiveData()
    private val _getReChatResponse: MutableLiveData<Response<String>> = MutableLiveData()
    private val _postChatResponse: MutableLiveData<Response<String>> = MutableLiveData()
    private val _postReChatResponse: MutableLiveData<Response<String>> = MutableLiveData()

    val getChatResponse: LiveData<Response<String>>
        get() = _getChatResponse
    val getReChatResponse: LiveData<Response<String>>
        get() = _getReChatResponse
    val postChatResponse: LiveData<Response<String>>
        get() = _postChatResponse
    val postReChatResponse: LiveData<Response<String>>
        get() = _postReChatResponse

    fun getChat(hashMap: HashMap<String, Any>, getComment: String) {
        viewModelScope.launch {
            try {
                val response = chatRepository.getChat(hashMap, getComment)
                _getChatResponse.value = response
            } catch (e: Exception) {
                Log.d("getChatError", e.toString())
            }
        }
    }

    fun getReChat(hashMap: HashMap<String, Any>, getReComment: String) {
        viewModelScope.launch {
            try {
                val response = chatRepository.getReChat(hashMap, getReComment)
                _getReChatResponse.value = response
            } catch (e: Exception) {
                Log.d("getReChatError", e.toString())
            }
        }
    }

    fun postChat(hashMap: HashMap<String, Any>, postComment: String) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postChat(hashMap, postComment)
                _postChatResponse.value = response
            } catch (e: Exception) {
                Log.d("postChatError", e.toString())
            }
        }
    }


    fun postReChat(hashMap: HashMap<String, Any>, postReComment: String) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postReChat(hashMap, postReComment)
                _postReChatResponse.value = response
            } catch (e: Exception) {
                Log.d("postReChatError", e.toString())
            }
        }
    }
}