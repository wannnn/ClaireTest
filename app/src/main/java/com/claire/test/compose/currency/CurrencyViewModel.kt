package com.claire.test.compose.currency

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.data.repo.CurrencyRepository
import com.claire.test.utils.KEY_CURRENCY_TYPE
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

/**
 * UiState for the currency screen.
 */
sealed interface CurrencyUiState {
    data object Loading : CurrencyUiState
    data class CurrencyList(
        val currencyList: List<CurrencyInfo> = listOf()
    ) : CurrencyUiState

    data object Empty : CurrencyUiState
}

@KoinViewModel
class CurrencyViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CurrencyRepository
) : ViewModel() {

    private var currencyType: String = savedStateHandle[KEY_CURRENCY_TYPE] ?: ""

    val uiState: StateFlow<CurrencyUiState> =
        repository.getData(CurrencyType.valueOf(currencyType)).map {
            if (it.isEmpty()) {
                CurrencyUiState.Empty
            } else {
                CurrencyUiState.CurrencyList(it)
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = CurrencyUiState.Loading,
        )
}