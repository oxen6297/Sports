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
    private val _groupResponse: MutableLiveData<Response<GroupPlayTab>> = MutableLiveData()
    private val _aloneResponse: MutableLiveData<Response<AlonePlay>> = MutableLiveData()
    private val _shopResponse: MutableLiveData<Response<ShopTab>> = MutableLiveData()
    private val _bestBoardResponse: MutableLiveData<Response<BestBoardTab>> = MutableLiveData()
    private val _communityResponse: MutableLiveData<Response<FreeBoardTab>> = MutableLiveData()
    private val _writeAlone: MutableLiveData<Response<WritePlayWith>> = MutableLiveData()
    private val _writeGroup: MutableLiveData<Response<WriteGroupPlay>> = MutableLiveData()
    private val _writeShop: MutableLiveData<Response<WriteShop>> = MutableLiveData()
    private val _writeCommunity: MutableLiveData<Response<WriteContent>> = MutableLiveData()

    val newsResponse: LiveData<Response<NewsList>>
        get() = _newsResponse
    val groupResponse: LiveData<Response<GroupPlayTab>>
        get() = _groupResponse
    val aloneResponse: LiveData<Response<AlonePlay>>
        get() = _aloneResponse
    val shopResponse: LiveData<Response<ShopTab>>
        get() = _shopResponse
    val bestBoardResponse: LiveData<Response<BestBoardTab>>
        get() = _bestBoardResponse
    val communityResponse: LiveData<Response<FreeBoardTab>>
        get() = _communityResponse
    val writeAlone: LiveData<Response<WritePlayWith>>
        get() = _writeAlone
    val writeGroup: LiveData<Response<WriteGroupPlay>>
        get() = _writeGroup
    val writeShop: LiveData<Response<WriteShop>>
        get() = _writeShop
    val writeCommunity: LiveData<Response<WriteContent>>
        get() = _writeCommunity

    fun writeCommunity(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.writeCommunity(hashMap)
                _writeCommunity.value = response
            } catch (e: Exception) {
                Log.d("writeComError", e.toString())
            }
        }
    }

    fun writeShop(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.writeShop(hashMap)
                _writeShop.value = response
            } catch (e: Exception) {
                Log.d("writeShopError", e.toString())
            }
        }
    }

    fun writeGroup(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.writeGroup(hashMap)
                _writeGroup.value = response
            } catch (e: Exception) {
                Log.d("writeGroupError", e.toString())
            }
        }
    }

    fun writeAlone(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.writeAlone(hashMap)
                _writeAlone.value = response
            } catch (e: Exception) {
                Log.d("writeAloneError", e.toString())
            }
        }
    }

    fun getCommunity(number: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCommunity(number)
                _communityResponse.value = response
            } catch (e: Exception) {
                Log.d("communityError", e.toString())
            }
        }
    }

    fun getNews() {
        viewModelScope.launch {
            try {
                val response = repository.getNews()
                _newsResponse.value = response
            }catch (e:Exception){
                Log.d("newsError",e.toString())
            }
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
