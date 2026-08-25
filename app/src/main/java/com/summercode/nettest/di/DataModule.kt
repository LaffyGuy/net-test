package com.summercode.nettest.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.summercode.nettest.data.local.ModeLocalDataSource
import com.summercode.nettest.data.local.db.AppDatabase
import com.summercode.nettest.data.repository.AppModeRepositoryImpl
import com.summercode.nettest.data.repository.MeasurementRepositoryImpl
import com.summercode.nettest.data.repository.SpeedTestRepositoryImpl
import com.summercode.nettest.domain.repository.AppModeRepository
import com.summercode.nettest.domain.repository.MeasurementRepository
import com.summercode.nettest.domain.repository.SpeedTestRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREFERENCES_FILE_NAME = "app_settings"

private const val DATABASE_NAME = "net_test.db"


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
            localDataSource = get(),
        )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
    single { get<AppDatabase>().speedResultDao() }
    single<MeasurementRepository> { MeasurementRepositoryImpl(speedResultDao = get()) }

    single<SpeedTestRepository> { SpeedTestRepositoryImpl(remoteDataSource = get()) }
}