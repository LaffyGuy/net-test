package com.summercode.nettest.di

import com.summercode.nettest.domain.usecase.ResolveAppModeUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { ResolveAppModeUseCase(appModeRepository = get()) }

}