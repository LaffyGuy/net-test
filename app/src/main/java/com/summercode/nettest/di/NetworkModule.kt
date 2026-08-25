package com.summercode.nettest.di

import com.summercode.nettest.data.remote.ConfigRemoteDataSource
import com.summercode.nettest.data.remote.SpeedTestRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val USER_AGENT = "NetTest/1.0 (Android)"

val networkModule = module {

    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                val json = Json { ignoreUnknownKeys = true }
                json(json)
                // GitHub raw віддає .json із заголовком text/plain (x-content-type-options: nosniff)
                json(json, contentType = ContentType.Text.Plain)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
            }
            install(UserAgent) {
                agent = USER_AGENT
            }
        }
    }

    single { ConfigRemoteDataSource(httpClient = get()) }

    single { SpeedTestRemoteDataSource(httpClient = get()) }

}