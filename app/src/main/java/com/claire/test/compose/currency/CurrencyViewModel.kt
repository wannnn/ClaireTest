package com.claire.test.compose.currency

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.data.repo.CurrencyRepository
import com.claire.test.utils.KEY_CURRENCY_TYPE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

/**
 * UiState for the currency screen.
 */
sealed interface CurrencyUiState {
    data object Loading : CurrencyUiState
    data class CurrencyList(
        val currencyList: List<CurrencyInfo> = listOf(),
    ) : CurrencyUiState

    data object Empty : CurrencyUiState
}

data class SearchUiState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val filteredCurrencyList: List<CurrencyInfo> = listOf(),
) {
    fun isSearchEmpty(): Boolean = filteredCurrencyList.isEmpty()
}

@KoinViewModel
class CurrencyViewModel(
    savedStateHandle: SavedStateHandle,
    repository: CurrencyRepository
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

    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchUiState.update { it.copy(searchQuery = query) }
        updateSearchResults(
            (uiState.value as? CurrencyUiState.CurrencyList)?.currencyList.orEmpty(), query
        )
    }

    fun onToggleSearch(active: Boolean) {
        _searchUiState.update { it.copy(isSearching = active) }
        onSearchQueryChange("")
    }

    private fun updateSearchResults(currencyList: List<CurrencyInfo>, query: String) {
        val filteredList = if (query.isEmpty()) {
            listOf()
        } else {
            currencyList.filter { currency ->
                val nameMatches = currency.name.startsWith(query, ignoreCase = true) ||
                        currency.name.contains(" $query", ignoreCase = true)
                val symbolMatches = currency.symbol.startsWith(query, ignoreCase = true)
                nameMatches || symbolMatches
            }
        }
        _searchUiState.update { it.copy(filteredCurrencyList = filteredList) }
    }
}