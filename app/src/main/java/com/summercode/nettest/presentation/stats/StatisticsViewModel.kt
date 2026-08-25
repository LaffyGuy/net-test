package com.summercode.nettest.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summercode.nettest.domain.model.Measurement
import com.summercode.nettest.domain.usecase.ObserveMeasurementsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class StatisticsViewModel(
    observeMeasurements: ObserveMeasurementsUseCase
): ViewModel() {

    val measurements: StateFlow<List<Measurement>> = observeMeasurements()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = emptyList(),
        )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }

}