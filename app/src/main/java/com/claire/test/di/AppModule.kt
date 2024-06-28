package com.claire.test.di

import com.claire.test.compose.currency.CurrencyViewModel
import com.claire.test.compose.demo.DemoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::DemoViewModel)
    viewModelOf(::CurrencyViewModel)
}