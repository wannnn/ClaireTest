package com.claire.test.compose.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.claire.test.R
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.ui.theme.ClaireTestTheme

@Composable
fun CurrencyScreen(uiState: CurrencyUiState) {
    when (uiState) {
        CurrencyUiState.Loading -> LoadingWheel()
        is CurrencyUiState.CurrencyList -> CurrencyList(uiState.currencyList)
        CurrencyUiState.Empty -> EmptyView()
    }
}

@Composable
fun LoadingWheel() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun CurrencyList(currencyList: List<CurrencyInfo>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(currencyList) {
            CurrencyItem(it)
            HorizontalDivider()
        }
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.no_data))
    }
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
                currencyList = listOf(
                    CurrencyInfo(
                        id = "BTC",
                        name = "Bitcoin",
                        symbol = "BTC"
                    ),
                )
            )
        )
    }
}