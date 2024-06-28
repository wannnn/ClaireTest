package com.claire.test.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.claire.test.compose.currency.CurrencyType

sealed class Screen(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object Demo : Screen(route = "demo")
    data object Currency : Screen(
        route = "currency/{currencyType}",
        navArguments = listOf(navArgument("currencyType") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(type: CurrencyType) = "currency/${type.name}"
    }
}