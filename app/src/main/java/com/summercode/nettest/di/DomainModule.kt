package com.summercode.nettest.di

import com.summercode.nettest.domain.usecase.ObserveMeasurementUseCase
import com.summercode.nettest.domain.usecase.ResolveAppModeUseCase
import com.summercode.nettest.domain.usecase.RunSpeedTestUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { ResolveAppModeUseCase(appModeRepository = get()) }

    factory { ObserveMeasurementUseCase(measurementRepository = get()) }

    factory {
        RunSpeedTestUseCase(
            speedTestRepository = get(),
            measurementRepository = get()
        )
    }

}