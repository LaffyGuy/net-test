package com.summercode.nettest.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.summercode.nettest.domain.model.AppMode
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

class ModeLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getMode(): AppMode? {
        val preferences = dataStore.data
            .catch { throwable ->
                if (throwable is IOException) emit(emptyPreferences()) else throw throwable
            }
            .first()

        val storedValue = preferences[KEY_MODE] ?: return null
        return AppMode.fromRaw(storedValue)
    }

    suspend fun saveMode(mode: AppMode) {
        dataStore.edit { preferences ->
            preferences[KEY_MODE] = mode.name
        }
    }

    private companion object {
        val KEY_MODE = stringPreferencesKey("app_mode")
    }

}