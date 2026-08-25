package com.summercode.nettest.data.mapper

import com.summercode.nettest.data.local.db.SpeedResultEntity
import com.summercode.nettest.domain.model.Measurement
import com.summercode.nettest.domain.model.SpeedTestResult

fun SpeedResultEntity.toMeasurement(): Measurement {
    return Measurement(
        id = id,
        timestampMillis = timestampMillis,
        averageMbps = averageMbps,
        peakMbps = peakMbps
    )
}

fun SpeedTestResult.toEntity(timestampMillis: Long): SpeedResultEntity {
    return SpeedResultEntity(
        timestampMillis = timestampMillis,
        averageMbps = averageMbps,
        peakMbps = peakMbps
    )
}