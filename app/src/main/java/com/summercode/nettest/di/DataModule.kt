package com.summercode.nettest.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.summercode.nettest.data.local.ModeLocalDataSource
import com.summercode.nettest.data.repository.AppModeRepositoryImpl
import com.summercode.nettest.domain.repository.AppModeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREFERENCES_FILE_NAME = "app_settings"

val dataModule = module {

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(PREFERENCES_FILE_NAME)
        }
    }

    single { ModeLocalDataSource(dataStore = get()) }

    single<AppModeRepository> {
        AppModeRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }

}