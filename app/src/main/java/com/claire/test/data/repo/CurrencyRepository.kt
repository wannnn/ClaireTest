package com.claire.test.data.repo

import com.claire.test.compose.currency.CurrencyType
import com.claire.test.data.model.CurrencyInfo
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun clearData()
    suspend fun insertData(): Boolean
    fun getData(type: CurrencyType): Flow<List<CurrencyInfo>>
}

