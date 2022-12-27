package com.example.sportscommunity.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sportscommunity.Repository.Repository
import com.example.sportscommunity.ViewModel.LikeViewModel

class LikeViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikeViewModel::class.java)){
            return LikeViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown viewModel Class")
    }
}
