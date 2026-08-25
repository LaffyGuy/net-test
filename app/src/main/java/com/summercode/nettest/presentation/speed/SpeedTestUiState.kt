package com.summercode.nettest.presentation.speed

import androidx.annotation.FloatRange
import com.summercode.nettest.domain.model.SpeedTestError

sealed interface SpeedTestUiState {

    data object Idle : SpeedTestUiState

    data class Running(
        val currentMbps: Double,
        @param:FloatRange(from = 0.0, to = 1.0)
        val progress: Float,
    ) : SpeedTestUiState

    data class Finished(
        val averageMbps: Double,
        val peakMbps: Double,
    ) : SpeedTestUiState

    data class Failed(val error: SpeedTestError) : SpeedTestUiState
}