package com.claire.test.compose.currency

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.data.repo.CurrencyRepository
import com.claire.test.utils.KEY_CURRENCY_TYPE
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
        val currencyList: PersistentList<CurrencyInfo> = persistentListOf(),
    ) : CurrencyUiState

    data object Empty : CurrencyUiState
}

data class SearchUiState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val filteredCurrencyList: PersistentList<CurrencyInfo> = persistentListOf(),
) {
    fun isSearchEmpty(): Boolean = filteredCurrencyList.isEmpty()
}

@KoinViewModel
class CurrencyViewModel(
    savedStateHandle: SavedStateHandle,
    repository: CurrencyRepository
) : ViewModel() {

    private var currencyType: String? = savedStateHandle[KEY_CURRENCY_TYPE]

    val uiState: StateFlow<CurrencyUiState> = currencyType?.let { type ->
        repository.getData(CurrencyType.valueOf(type))
            .catch { emit(emptyList()) }
            .map {
                if (it.isEmpty()) {
                    CurrencyUiState.Empty
                } else {
                    CurrencyUiState.CurrencyList(it.toPersistentList())
                }
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5_000),
                initialValue = CurrencyUiState.Loading,
            )
    } ?: MutableStateFlow(CurrencyUiState.Empty)

    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchUiState.update { it.copy(searchQuery = query) }
        updateSearchResults(query)
    }

    fun onToggleSearch(active: Boolean) {
        _searchUiState.update { it.copy(isSearching = active) }
        onSearchQueryChange("")
    }

    private fun updateSearchResults(query: String) {
        val originalList = (uiState.value as? CurrencyUiState.CurrencyList)?.currencyList.orEmpty()
        val filteredList = if (query.isEmpty()) {
            persistentListOf()
        } else {
            originalList.filter { currency ->
                val nameMatches = currency.name.startsWith(query, ignoreCase = true) ||
                        currency.name.contains(" $query", ignoreCase = true)
                val symbolMatches = currency.symbol.startsWith(query, ignoreCase = true)
                nameMatches || symbolMatches
            }.toPersistentList()
        }
        _searchUiState.update { it.copy(filteredCurrencyList = filteredList) }
    }
}