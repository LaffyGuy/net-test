package com.summercode.nettest.domain.repository

import com.summercode.nettest.domain.model.AppMode
import com.summercode.nettest.domain.model.AppModeResult

interface AppModeRepository {

    suspend fun getCachedMode(): AppMode?

    suspend fun fetchAndCacheMode(): AppModeResult

}