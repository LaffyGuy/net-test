package com.summercode.nettest.presentation.speed

import com.summercode.nettest.domain.model.SpeedTestError

sealed interface SpeedTestUiState {

    data object Idle : SpeedTestUiState

    data class Running(
        val currentMbps: Double,
        val elapsedMillis: Long,
    ) : SpeedTestUiState

    data class Finished(
        val averageMbps: Double,
        val peakMbps: Double,
    ) : SpeedTestUiState

    data class Failed(val error: SpeedTestError) : SpeedTestUiState

}