package com.claire.test.compose.currency

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.claire.test.data.CurrencyInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CurrencyViewModel(
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private var currencyType: String? = savedStateHandle["currencyType"]

    private val _currencyList = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val currencyList: Flow<List<CurrencyInfo>> get() = _currencyList

    init {
        println(currencyType)
        fetchData()
    }

    private fun fetchData() {

    }
}