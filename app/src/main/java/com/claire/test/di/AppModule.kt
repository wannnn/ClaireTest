package com.claire.test.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.claire.test.utils.PREFERENCE_DATA_STORE
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.claire.test")
class AppModule

@Single
internal fun providePreferencesDataStore(context: Context) = PreferenceDataStoreFactory.create(
    produceFile = { context.preferencesDataStoreFile(PREFERENCE_DATA_STORE) }
)