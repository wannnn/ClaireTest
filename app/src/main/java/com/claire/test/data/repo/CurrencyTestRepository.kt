package com.claire.test.data.repo

import com.claire.test.compose.currency.CurrencyType
import com.claire.test.data.mock.testData
import com.claire.test.data.model.CurrencyInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrencyTestRepository : CurrencyRepository {
    override fun clearData(): Flow<Unit> = flow {
        emit(Unit)
    }

    override fun insertData(): Flow<Boolean> = flow {
        emit(true)
    }

    override fun getData(type: CurrencyType): Flow<List<CurrencyInfo>> = flow {
        emit(testData)
    }
}