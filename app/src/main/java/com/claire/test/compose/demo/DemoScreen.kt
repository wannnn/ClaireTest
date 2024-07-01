package com.claire.test.compose.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.claire.test.R
import com.claire.test.compose.currency.CurrencyType
import com.claire.test.ui.theme.ClaireTestTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DemoScreen(
    viewModel: DemoViewModel = koinViewModel(),
    onPageClick: (type: CurrencyType) -> Unit = {}
) {
    DemoScreen(
        onClearData = viewModel::clearData,
        onInsertData = viewModel::insertData,
        onPageClick = onPageClick
    )
}

@Composable
fun DemoScreen(
    onClearData: () -> Unit = {},
    onInsertData: () -> Unit = {},
    onPageClick: (type: CurrencyType) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DemoBtn(
            icon = Icons.Filled.Delete,
            text = stringResource(id = R.string.clear_data),
            onClick = onClearData
        )
        DemoBtn(
            icon = Icons.Filled.Add,
            text = stringResource(id = R.string.insert_data),
            onClick = onInsertData
        )
        DemoBtn(
            icon = Icons.Filled.FavoriteBorder,
            text = stringResource(id = R.string.crypto_currency),
            onClick = { onPageClick(CurrencyType.Crypto) }
        )
        DemoBtn(
            icon = Icons.Filled.Favorite,
            text = stringResource(id = R.string.fiat_currency),
            onClick = { onPageClick(CurrencyType.Fiat) }
        )
        DemoBtn(
            icon = Icons.Filled.Face,
            text = stringResource(id = R.string.all_currency),
            onClick = { onPageClick(CurrencyType.All) }
        )
    }
}

@Composable
fun DemoBtn(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun DemoScreenPreview() {
    ClaireTestTheme {
        DemoScreen()
    }
}