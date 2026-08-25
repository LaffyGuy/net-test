package com.summercode.nettest.domain.usecase

import com.summercode.nettest.domain.model.SpeedSample
import com.summercode.nettest.domain.model.SpeedTestResult
import com.summercode.nettest.domain.repository.MeasurementRepository
import com.summercode.nettest.domain.repository.SpeedTestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RunSpeedTestUseCase(
    private val speedTestRepository: SpeedTestRepository,
    private val measurementRepository: MeasurementRepository,
) {

    operator fun invoke(): Flow<SpeedSample> = flow {
        var peakMbps = 0.0
        var lastSample: SpeedSample? = null

        speedTestRepository.measure(TEST_DURATION_MILLIS).collect { sample ->
            if (sample.currentMbps > peakMbps) {
                peakMbps = sample.currentMbps
            }
            lastSample = sample
            emit(sample)
        }

        val finalSample = lastSample ?: return@flow
        measurementRepository.save(
            SpeedTestResult(
                averageMbps = toMbps(finalSample.totalBytes, finalSample.elapsedMillis),
                peakMbps = peakMbps,
            )
        )
    }

    companion object {
        const val TEST_DURATION_MILLIS = 10_000L
    }
}

private fun toMbps(bytes: Long, millis: Long): Double {
    if (millis <= 0) return 0.0
    return bytes * 8 * 1000.0 / millis / 1_000_000.0
}