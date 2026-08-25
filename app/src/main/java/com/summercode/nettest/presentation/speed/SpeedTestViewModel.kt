package com.summercode.nettest.presentation.speed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summercode.nettest.domain.model.SpeedTestError
import com.summercode.nettest.domain.model.SpeedTestException
import com.summercode.nettest.domain.usecase.RunSpeedTestUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpeedTestViewModel(
    private val runSpeedTest: RunSpeedTestUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<SpeedTestUiState>(SpeedTestUiState.Idle)
    val uiState: StateFlow<SpeedTestUiState> = _uiState.asStateFlow()

    private var measurementJob: Job? = null

    fun start() {
        if (measurementJob?.isActive == true) return

        _uiState.value = SpeedTestUiState.Running(currentMbps = 0.0, progress = 0f)

        measurementJob = viewModelScope.launch {
            var peakMbps = 0.0
            var averageMbps = 0.0

            try {
                runSpeedTest().collect { sample ->
                    if (sample.currentMbps > peakMbps) {
                        peakMbps = sample.currentMbps
                    }
                    averageMbps = toMbps(sample.totalBytes, sample.elapsedMillis)

                    _uiState.value = SpeedTestUiState.Running(
                        currentMbps = sample.currentMbps,
                        progress = sample.elapsedMillis.toProgress(),
                    )
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (speedTestException: SpeedTestException) {
                _uiState.value = SpeedTestUiState.Failed(speedTestException.error)
                return@launch
            } catch (throwable: Throwable) {
                _uiState.value = SpeedTestUiState.Failed(
                    SpeedTestError.Unexpected(throwable::class.simpleName ?: "Unknown")
                )
                return@launch
            }

            _uiState.value = SpeedTestUiState.Finished(
                averageMbps = averageMbps,
                peakMbps = peakMbps,
            )
        }
    }

    fun stop() {
        measurementJob?.cancel()
        measurementJob = null

        if (_uiState.value is SpeedTestUiState.Running) {
            _uiState.value = SpeedTestUiState.Idle
        }
    }

}

private fun toMbps(bytes: Long, millis: Long): Double {
    if (millis <= 0) return 0.0
    return bytes * 8 * 1000.0 / millis / 1_000_000.0
}

private fun Long.toProgress(): Float =
    (this.toFloat() / RunSpeedTestUseCase.TEST_DURATION_MILLIS).coerceIn(0f, 1f)