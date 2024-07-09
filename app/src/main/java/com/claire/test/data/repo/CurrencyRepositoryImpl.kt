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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class CurrencyRepositoryImpl(private val localDataSource: CurrencyDataStore) : CurrencyRepository {
    override suspend fun clearData() {
        withContext(Dispatchers.IO) {
            localDataSource.clearData()
        }
    }

    override suspend fun insertData(): Boolean {
        return withContext(Dispatchers.IO) {
            localDataSource.insertData(dataA, dataB)
        }
    }

    override fun getData(type: CurrencyType): Flow<List<CurrencyInfo>> {
        return when (type) {
            CurrencyType.Crypto -> localDataSource.getData(KEY_CRYPTO_CURRENCY)
                .map { data ->
                    delay(1000) // simulate api call
                    data
                }
                .flowOn(Dispatchers.IO)

            CurrencyType.Fiat -> localDataSource.getData(KEY_FIAT_CURRENCY)
                .map { data ->
                    delay(1000) // simulate api call
                    data
                }
                .flowOn(Dispatchers.IO)

            CurrencyType.All -> {
                combine(
                    localDataSource.getData(KEY_CRYPTO_CURRENCY),
                    localDataSource.getData(KEY_FIAT_CURRENCY)
                ) { cryptoData, fiatData ->
                    delay(1000) // simulate api call
                    cryptoData + fiatData
                }.flowOn(Dispatchers.IO)
            }
        }
    }
}