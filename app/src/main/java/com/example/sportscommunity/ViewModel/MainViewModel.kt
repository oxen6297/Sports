package com.example.sportscommunity.ViewModel

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportscommunity.*
import com.example.sportscommunity.Repository.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _newsResponse: MutableLiveData<Response<NewsList>> = MutableLiveData()
    val newsResponse: LiveData<Response<NewsList>> = _newsResponse

    private val _groupResponse: MutableLiveData<Response<GroupPlayTab>> = MutableLiveData()
    val groupResponse: LiveData<Response<GroupPlayTab>> = _groupResponse

    private val _aloneResponse: MutableLiveData<Response<AlonePlay>> = MutableLiveData()
    val aloneResponse: LiveData<Response<AlonePlay>> = _aloneResponse

    private val _shopResponse: MutableLiveData<Response<ShopTab>> = MutableLiveData()
    val shopResponse: LiveData<Response<ShopTab>> = _shopResponse

    private val _bestBoardResponse: MutableLiveData<Response<BestBoardTab>> = MutableLiveData()
    val bestBoardResponse: LiveData<Response<BestBoardTab>> = _bestBoardResponse

    private val _communityResponse: MutableLiveData<Response<FreeBoardTab>> = MutableLiveData()
    val communityResponse: LiveData<Response<FreeBoardTab>> = _communityResponse

    fun getCommunity(number: String) {
        try {
            viewModelScope.launch {
                val response = repository.getCommunity(number)
                _communityResponse.value = response
            }
        } catch (e: Exception) {
            Log.d("communityError", e.toString())
        }
    }

    fun getNews() {
        viewModelScope.launch {
            val response = repository.getNews()
            _newsResponse.value = response
        }
    }

    fun getGroup() {
        viewModelScope.launch {
            try {
                val response = repository.getGroup()
                _groupResponse.value = response
            } catch (e: Exception) {
                Log.d("groupError", e.toString())
            }
        }
    }

    fun getAlone() {
        viewModelScope.launch {
            try {
                val response = repository.getAlone()
                _aloneResponse.value = response
            } catch (e: Exception) {
                Log.d("aloneError", e.toString())
            }
        }
    }

    fun getShop() {
        viewModelScope.launch {
            try {
                val response = repository.getShop()
                _shopResponse.value = response
            } catch (e: Exception) {
                Log.d("shopError", e.toString())
            }
        }
    }

    fun getBestBoard() {
        viewModelScope.launch {
            try {
                val response = repository.getBestBoard()
                _bestBoardResponse.value = response
            } catch (e: Exception) {
                Log.d("bestBoardError", e.toString())
            }
        }
    }
}
