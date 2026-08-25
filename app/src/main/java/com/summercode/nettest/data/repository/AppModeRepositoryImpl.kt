package com.summercode.nettest.data.repository

import com.summercode.nettest.data.local.ModeLocalDataSource
import com.summercode.nettest.data.mapper.toAppMode
import com.summercode.nettest.data.remote.ConfigRemoteDataSource
import com.summercode.nettest.domain.model.AppMode
import com.summercode.nettest.domain.model.AppModeResult
import com.summercode.nettest.domain.model.ConfigError
import com.summercode.nettest.domain.model.ConfigException
import com.summercode.nettest.domain.repository.AppModeRepository
import kotlinx.coroutines.CancellationException
import java.io.IOException

class AppModeRepositoryImpl(
    private val remoteDataSource: ConfigRemoteDataSource,
    private val localDataSource: ModeLocalDataSource
): AppModeRepository {

    override suspend fun getCachedMode(): AppMode? = localDataSource.getMode()

    override suspend fun fetchAndCacheMode(): AppModeResult =
        try {
           val mode = remoteDataSource.fetchConfig().toAppMode()
            localDataSource.saveMode(mode)
            AppModeResult.Success(mode)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (configException: ConfigException) {
            AppModeResult.Failure(configException.error)
        } catch (_: IOException) {
            AppModeResult.Failure(ConfigError.Unexpected("CacheWriteFailed"))
        }


}