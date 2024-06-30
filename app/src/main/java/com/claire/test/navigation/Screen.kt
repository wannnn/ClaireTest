package com.claire.test.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.claire.test.compose.currency.CurrencyType
import com.claire.test.utils.KEY_CURRENCY_TYPE

sealed class Screen(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object Demo : Screen(route = "demo")
    data object Currency : Screen(
        route = "currency/{$KEY_CURRENCY_TYPE}",
        navArguments = listOf(navArgument(KEY_CURRENCY_TYPE) {
            type = NavType.StringType
        })
    ) {
        fun createRoute(type: CurrencyType) = "currency/${type.name}"
    }
}