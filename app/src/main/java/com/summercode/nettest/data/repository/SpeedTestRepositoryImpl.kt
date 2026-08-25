package com.summercode.nettest.data.repository

import com.summercode.nettest.data.remote.SpeedTestRemoteDataSource
import com.summercode.nettest.domain.model.SpeedSample
import com.summercode.nettest.domain.model.SpeedTestError
import com.summercode.nettest.domain.model.SpeedTestException
import com.summercode.nettest.domain.repository.SpeedTestRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.nio.channels.UnresolvedAddressException

private const val SAMPLE_INTERVAL_MILLIS = 500L
private const val BITS_PER_BYTE = 8
private const val BITS_PER_MEGABIT = 1_000_000.0

class SpeedTestRepositoryImpl(
    private val remoteDataSource: SpeedTestRemoteDataSource
): SpeedTestRepository {

    override fun measure(durationMillis: Long): Flow<SpeedSample> = flow {
        val startedAt = System.nanoTime()

        var totalBytes = 0L
        var bytesInWindow = 0L
        var windowStartedAt = startedAt

        try {
            remoteDataSource.download { bytesRead ->
                currentCoroutineContext().ensureActive()

                totalBytes += bytesRead
                bytesInWindow += bytesRead

                val now = System.nanoTime()
                val windowMillis = (now - windowStartedAt).toMillis()
                val elapsedMillis = (now - startedAt).toMillis()

                if (windowMillis >= SAMPLE_INTERVAL_MILLIS) {
                    emit(
                        SpeedSample(
                            currentMbps = toMbps(bytesInWindow, windowMillis),
                            totalBytes = totalBytes,
                            elapsedMillis = elapsedMillis,
                        )
                    )
                    bytesInWindow = 0
                    windowStartedAt = now
                }

                if (elapsedMillis >= durationMillis) {
                    throw MeasurementCompleted()
                }
            }
        } catch (_: MeasurementCompleted) {
            // Заплановане завершення: минув час тесту, з'єднання закривається виходом з execute.
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (speedTestException: SpeedTestException) {
            throw speedTestException
        } catch (throwable: Throwable) {
            throw SpeedTestException(throwable.toSpeedTestError())
        }

        val elapsedMillis = (System.nanoTime() - startedAt).toMillis()
        emit(
            SpeedSample(
                currentMbps = 0.0,
                totalBytes = totalBytes,
                elapsedMillis = elapsedMillis,
            )
        )
    }.flowOn(Dispatchers.IO)

    private class MeasurementCompleted : Throwable(null, null, false, false)

}

private fun Long.toMillis(): Long = this / 1_000_000

private fun toMbps(bytes: Long, millis: Long): Double {
    if (millis <= 0) return 0.0
    return bytes * BITS_PER_BYTE * 1000.0 / millis / BITS_PER_MEGABIT
}

private fun Throwable.toSpeedTestError(): SpeedTestError = when (this) {
    is UnresolvedAddressException, is IOException -> SpeedTestError.NoConnection
    else -> SpeedTestError.Unexpected(this::class.simpleName ?: "Unknown")
}