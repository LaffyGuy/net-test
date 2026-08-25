package com.summercode.nettest.di

import com.summercode.nettest.presentation.root.RootViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::RootViewModel)
}