package com.claire.test.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyInfo(
    val id: String = "",
    val name: String = "",
    val symbol: String = "",
    val code: String? = null,
)