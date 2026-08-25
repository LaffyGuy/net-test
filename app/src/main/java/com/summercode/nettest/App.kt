package com.summercode.nettest

import android.app.Application
import com.summercode.nettest.di.dataModule
import com.summercode.nettest.di.domainModule
import com.summercode.nettest.di.networkModule
import com.summercode.nettest.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(networkModule, dataModule, domainModule, presentationModule)
        }

    }

}