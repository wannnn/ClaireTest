package com.claire.test.data.repo

import com.claire.test.compose.currency.CurrencyType
import com.claire.test.data.mock.dataA
import com.claire.test.data.mock.dataB
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.data.source.CurrencyDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Single

@Single
class CurrencyRepository(private val localDataSource: CurrencyDataStore) {
    fun clearData(): Flow<Unit> = flow {
        emit(localDataSource.clearData())
    }.flowOn(Dispatchers.IO)

    fun insertData(): Flow<Boolean> = flow {
        emit(localDataSource.insertData(dataA, dataB))
    }.flowOn(Dispatchers.IO)

    fun getData(type: CurrencyType): Flow<List<CurrencyInfo>> = flow {
        delay(1000) // simulate api call
        emit(localDataSource.getData(type))
    }.flowOn(Dispatchers.IO)
}

