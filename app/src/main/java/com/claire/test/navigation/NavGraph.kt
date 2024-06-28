package com.claire.test.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.claire.test.compose.currency.CurrencyScreen
import com.claire.test.compose.currency.CurrencyViewModel
import com.claire.test.compose.demo.DemoScreen
import com.claire.test.compose.demo.DemoViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MyNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Demo.route,
        modifier = modifier
    ) {
        composable(route = Screen.Demo.route) {
            val viewModel: DemoViewModel = getViewModel()
            DemoScreen(
                onClearData = { viewModel.clearData() },
                onInsertData = { viewModel.insertData() },
                onPageClick = { navController.navigate(Screen.Currency.createRoute(it)) }
            )
        }
        composable(
            route = Screen.Currency.route,
            arguments = Screen.Currency.navArguments
        ) {
            val viewModel: CurrencyViewModel = getViewModel()
            CurrencyScreen()
        }
    }
}
