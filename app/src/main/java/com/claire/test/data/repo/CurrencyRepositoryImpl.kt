package com.claire.test.data.repo

import com.claire.test.compose.currency.CurrencyType
import com.claire.test.data.mock.dataA
import com.claire.test.data.mock.dataB
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.data.source.CurrencyDataStore
import com.claire.test.utils.KEY_CRYPTO_CURRENCY
import com.claire.test.utils.KEY_FIAT_CURRENCY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Single

@Single
class CurrencyRepositoryImpl(private val localDataSource: CurrencyDataStore) : CurrencyRepository {
    override fun clearData(): Flow<Unit> = flow {
        emit(localDataSource.clearData())
    }.flowOn(Dispatchers.IO)

    override fun insertData(): Flow<Boolean> = flow {
        emit(localDataSource.insertData(dataA, dataB))
    }.flowOn(Dispatchers.IO)

    override fun getData(type: CurrencyType): Flow<List<CurrencyInfo>> = flow {
        delay(1000) // simulate api call
        val data = when (type) {
            CurrencyType.Crypto -> localDataSource.getData(KEY_CRYPTO_CURRENCY)
            CurrencyType.Fiat -> localDataSource.getData(KEY_FIAT_CURRENCY)
            CurrencyType.All -> {
                val cryptoData = localDataSource.getData(KEY_CRYPTO_CURRENCY)
                val fiatData = localDataSource.getData(KEY_FIAT_CURRENCY)
                cryptoData + fiatData
            }
        }
        emit(data)
    }.flowOn(Dispatchers.IO)
}