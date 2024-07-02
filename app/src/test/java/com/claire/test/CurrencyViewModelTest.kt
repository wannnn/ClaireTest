package com.claire.test

import androidx.lifecycle.SavedStateHandle
import com.claire.test.compose.currency.CurrencyUiState
import com.claire.test.compose.currency.CurrencyViewModel
import com.claire.test.data.repo.CurrencyTestRepository
import com.claire.test.utils.KEY_CURRENCY_TYPE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CurrencyViewModelTest {

    private lateinit var viewModel: CurrencyViewModel
    private val testRepo = CurrencyTestRepository()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        viewModel = CurrencyViewModel(
            savedStateHandle = SavedStateHandle(initialState = mapOf(KEY_CURRENCY_TYPE to "All")),
            repository = testRepo
        )
    }

    @Test
    fun test_state_is_initially_loading() = runTest {
        assertEquals(
            CurrencyUiState.Loading,
            viewModel.uiState.value
        ) // Assert on the initial value
    }

    @Test
    fun test_ui_state_contains_currency_list() = runTest {
        val state = viewModel.uiState.first()

        assert(state is CurrencyUiState.CurrencyList)
        assertEquals(5, (state as CurrencyUiState.CurrencyList).currencyList.size)
    }

    @Test
    fun test_ui_state_is_empty_when_saved_state_is_empty() = runTest {
        val emptyViewModel = CurrencyViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = testRepo
        )

        assertEquals(CurrencyUiState.Empty, emptyViewModel.uiState.value)
    }

    @Test
    fun test_toggle_search() = runTest {
        viewModel.onToggleSearch(true)

        val searchUiState = viewModel.searchUiState.value

        assert(searchUiState.isSearching)
    }

    @Test
    fun test_search_by_name_starts_with() = runTest {
        viewModel.uiState.first()
        viewModel.onToggleSearch(true)
        viewModel.onSearchQueryChange("Bit")

        val searchUiState = viewModel.searchUiState.value

        assertEquals(1, searchUiState.filteredCurrencyList.size)
        assertEquals("Bitcoin", searchUiState.filteredCurrencyList.first().name)
    }

    @Test
    fun test_search_by_name_contains_with_space_prefix() = runTest {
        viewModel.uiState.first()
        viewModel.onToggleSearch(true)
        viewModel.onSearchQueryChange("Class")

        val searchUiState = viewModel.searchUiState.value

        assertEquals(1, searchUiState.filteredCurrencyList.size)
        assertEquals("Ethereum Classic", searchUiState.filteredCurrencyList.first().name)
    }

    @Test
    fun test_search_by_symbol_starts_with() = runTest {
        viewModel.uiState.first()
        viewModel.onToggleSearch(true)
        viewModel.onSearchQueryChange("ET")

        val searchUiState = viewModel.searchUiState.value

        assertEquals(2, searchUiState.filteredCurrencyList.size)
        assertEquals("Ethereum", searchUiState.filteredCurrencyList[0].name)
        assertEquals("Ethereum Classic", searchUiState.filteredCurrencyList[1].name)
    }
}