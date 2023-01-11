package com.example.sportscommunity.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sportscommunity.repository.Repository
import com.example.sportscommunity.viewmodel.LoginAndSignViewModel
import com.example.sportscommunity.viewmodel.MyPageViewModel

class MyPageViewModelFactory (
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)){
            return MyPageViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown viewModel Class")
    }
}