package com.summercode.nettest.data.repository

import com.summercode.nettest.data.local.db.SpeedResultDao
import com.summercode.nettest.data.mapper.toEntity
import com.summercode.nettest.data.mapper.toMeasurement
import com.summercode.nettest.domain.model.Measurement
import com.summercode.nettest.domain.model.SpeedTestResult
import com.summercode.nettest.domain.repository.MeasurementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MeasurementRepositoryImpl(
    private val speedResultDao: SpeedResultDao
): MeasurementRepository {

    override fun observeAll(): Flow<List<Measurement>> {
        return speedResultDao.observeAll().map { entities -> entities.map { it.toMeasurement() } }
    }

    override suspend fun save(result: SpeedTestResult) {
        speedResultDao.insert(result.toEntity(timestampMillis = System.currentTimeMillis()))
    }
}