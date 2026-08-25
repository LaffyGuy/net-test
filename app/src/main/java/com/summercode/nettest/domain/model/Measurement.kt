package com.summercode.nettest.domain.model

data class Measurement(
    val id: Long,
    val timestampMillis: Long,
    val averageMbps: Double,
    val peakMbps: Double
)