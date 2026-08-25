package com.summercode.nettest.data.remote

import com.summercode.nettest.domain.model.SpeedTestError
import com.summercode.nettest.domain.model.SpeedTestException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.remaining
import io.ktor.utils.io.readRemaining

class SpeedTestRemoteDataSource(
    private val httpClient: HttpClient
) {

    suspend fun download(onBytesRead: suspend (Long) -> Unit) {
        httpClient.prepareGet(DOWNLOAD_URL) {
            timeout {
                requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
                socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS
            }
        }.execute { response ->
            if (!response.status.isSuccess()) {
                throw SpeedTestException(toStatusError(response.status.value))
            }

            val channel: ByteReadChannel = response.bodyAsChannel()

            while (!channel.isClosedForRead) {
                val chunk = channel.readRemaining(CHUNK_SIZE)
                val bytesRead = chunk.remaining
                chunk.close()

                if (bytesRead > 0) {
                    onBytesRead(bytesRead)
                }
            }
        }
    }

    private fun toStatusError(statusCode: Int): SpeedTestError =
        when (statusCode) {
            in 400..499 -> SpeedTestError.ClientError(statusCode)
            else -> SpeedTestError.ServerError(statusCode)
        }

    private companion object {
        const val DOWNLOAD_URL = "https://proof.ovh.net/files/1Gb.dat"
        const val CHUNK_SIZE = 64L * 1024
        const val REQUEST_TIMEOUT_MILLIS = 60_000L
        const val SOCKET_TIMEOUT_MILLIS = 15_000L
    }

}