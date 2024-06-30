package com.claire.test.compose.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.claire.test.data.repo.CurrencyRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DemoViewModel(private val repository: CurrencyRepository): ViewModel() {

    fun clearData() {
        repository.clearData()
            .catch { println("Failed to clear data") }
            .launchIn(viewModelScope)
    }

    fun insertData() {
        repository.insertData()
            .catch { println("Failed to insert data") }
            .onEach { println(it) }
            .launchIn(viewModelScope)
    }
}