package com.claire.test.compose.currency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.claire.test.ui.theme.ClaireTestTheme

@Composable
fun CurrencyScreen() {
    EmptyView()
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No Data")
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyScreenPreview() {
    ClaireTestTheme {
        CurrencyScreen()
    }
}