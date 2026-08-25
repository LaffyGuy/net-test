package com.summercode.nettest.data.remote

import com.summercode.nettest.data.remote.dto.AppConfigDto
import com.summercode.nettest.domain.model.ConfigError
import com.summercode.nettest.domain.model.ConfigException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import java.io.IOException
import java.nio.channels.UnresolvedAddressException

class ConfigRemoteDataSource(
    private val httpClient: HttpClient
) {

    suspend fun fetchConfig(): AppConfigDto {
        val response = request()

        if (!response.status.isSuccess()) {
            throw ConfigException(toStatusError(response.status.value))
        }

        return deserialize(response)
    }


    private suspend fun request(): HttpResponse =
        try {
              httpClient.get(CONFIG_URL)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (throwable: Throwable) {
            throw ConfigException(throwable.toTransportError())
        }

    private suspend fun deserialize(response: HttpResponse): AppConfigDto =
        try {
           response.body<AppConfigDto>()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (throwable: Throwable) {
            val error = if (throwable is IOException) {
                ConfigError.NoConnection
            } else {
                ConfigError.Malformed
            }

            throw ConfigException(error)
        }

    private fun toStatusError(statusCode: Int): ConfigError =
        when (statusCode) {
            in 400..499 -> ConfigError.ClientError(statusCode)
            else -> ConfigError.ServerError(statusCode)
        }

    private fun Throwable.toTransportError(): ConfigError = when(this) {
        is UnresolvedAddressException, is IOException -> ConfigError.NoConnection
        else -> ConfigError.Unexpected(this::class.simpleName ?: "Unknown")
    }

    private companion object {
        const val CONFIG_URL = "https://raw.githubusercontent.com/LaffyGuy/net-test/main/config/app_config.json"
    }

}