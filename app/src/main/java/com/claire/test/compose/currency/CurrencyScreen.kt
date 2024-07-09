package com.claire.test.compose.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.claire.test.R
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.ui.theme.ClaireTestTheme
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()

    CurrencyScreen(
        uiState = uiState,
        searchUiState = searchUiState,
        onQueryChange = viewModel::onSearchQueryChange,
        onSearch = viewModel::onSearchQueryChange,
        onActiveChanged = viewModel::onToggleSearch
    )
}

@Composable
fun CurrencyScreen(
    uiState: CurrencyUiState,
    searchUiState: SearchUiState,
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onActiveChanged: (Boolean) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchView(
            uiState = searchUiState,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            onActiveChanged = onActiveChanged,
        )
        when (uiState) {
            CurrencyUiState.Loading -> CircularProgressIndicator()
            is CurrencyUiState.CurrencyList -> CurrencyList(
                currencyList = uiState.currencyList
            )

            CurrencyUiState.Empty -> EmptyView(stringResource(id = R.string.no_data))
        }
    }

}

@Composable
fun CurrencyList(currencyList: List<CurrencyInfo>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = currencyList,
            key = { it.id }
        ) {
            CurrencyItem(it)
            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChanged: (Boolean) -> Unit,
) {
    SearchBar(
        query = uiState.searchQuery,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = uiState.isSearching,
        onActiveChange = onActiveChanged,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.search)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        shadowElevation = 4.dp,
        windowInsets = WindowInsets(top = 0.dp)
    ) {
        if (uiState.isSearchEmpty()) {
            EmptyView(stringResource(id = R.string.no_results))
        } else {
            CurrencyList(currencyList = uiState.filteredCurrencyList)
        }
    }
}

@Composable
fun EmptyView(message: String) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CurrencyItem(info: CurrencyInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            text = info.symbol,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(text = info.name)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyScreenPreview() {
    ClaireTestTheme {
        CurrencyScreen(
            uiState = CurrencyUiState.CurrencyList(
                currencyList = persistentListOf(
                    CurrencyInfo(
                        id = "BTC",
                        name = "Bitcoin",
                        symbol = "BTC"
                    ),
                )
            ),
            searchUiState = SearchUiState()
        )
    }
}