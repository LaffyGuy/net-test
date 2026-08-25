package com.summercode.nettest.di

import com.summercode.nettest.presentation.root.RootViewModel
import com.summercode.nettest.presentation.speed.SpeedTestViewModel
import com.summercode.nettest.presentation.stats.StatisticsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    viewModelOf(::RootViewModel)

    viewModelOf(::SpeedTestViewModel)

    viewModelOf(::StatisticsViewModel)
}