package com.summercode.nettest.domain.repository

import com.summercode.nettest.domain.model.Measurement
import com.summercode.nettest.domain.model.SpeedTestResult
import kotlinx.coroutines.flow.Flow

interface MeasurementRepository {

    fun observeAll(): Flow<List<Measurement>>

    suspend fun save(result: SpeedTestResult)

}