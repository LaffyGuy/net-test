package com.summercode.nettest.domain.usecase

import com.summercode.nettest.domain.model.Measurement
import com.summercode.nettest.domain.repository.MeasurementRepository
import kotlinx.coroutines.flow.Flow

class ObserveMeasurementsUseCase(
    private val measurementRepository: MeasurementRepository
) {

    operator fun invoke(): Flow<List<Measurement>> {
        return measurementRepository.observeAll()
    }

}