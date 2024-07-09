package com.claire.test.compose.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.claire.test.data.repo.CurrencyRepository
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DemoViewModel(private val repository: CurrencyRepository) : ViewModel() {

    fun clearData() = viewModelScope.launch {
        repository.clearData()
    }

    fun insertData() = viewModelScope.launch {
        repository.insertData()
    }
}