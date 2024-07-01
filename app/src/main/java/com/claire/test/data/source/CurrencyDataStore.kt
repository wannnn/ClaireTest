package com.claire.test.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.claire.test.data.model.CurrencyInfo
import com.claire.test.utils.KEY_CRYPTO_CURRENCY
import com.claire.test.utils.KEY_FIAT_CURRENCY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class CurrencyDataStore(private val dataStore: DataStore<Preferences>) {

    suspend fun clearData() {
        runCatching {
            dataStore.edit {
                it.clear()
            }
        }
    }

    suspend fun insertData(dataSetA: List<CurrencyInfo>, dataSetB: List<CurrencyInfo>): Boolean {
        return runCatching {
            dataStore.edit {
                it[stringPreferencesKey(KEY_CRYPTO_CURRENCY)] = Json.encodeToString(dataSetA)
                it[stringPreferencesKey(KEY_FIAT_CURRENCY)] = Json.encodeToString(dataSetB)
            }
        }.isSuccess
    }

    suspend fun getData(key: String): List<CurrencyInfo> {
        return runCatching {
            dataStore.data
                .map { preferences ->
                    val jsonStr = preferences[stringPreferencesKey(key)].orEmpty()
                    Json.decodeFromString<List<CurrencyInfo>>(jsonStr)
                }.first()
        }.getOrElse { emptyList() }
    }
}