package com.summercode.nettest.domain.usecase

import com.summercode.nettest.domain.model.AppModeResult
import com.summercode.nettest.domain.repository.AppModeRepository

class ResolveAppModeUseCase(
    private val appModeRepository: AppModeRepository
) {

    suspend operator fun invoke(): AppModeResult {
        val cachedMode = appModeRepository.getCachedMode()
        if (cachedMode != null) {
            return AppModeResult.Success(cachedMode)
        }
        return appModeRepository.fetchAndCacheMode()
    }

}