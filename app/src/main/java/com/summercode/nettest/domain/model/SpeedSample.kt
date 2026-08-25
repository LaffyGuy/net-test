package com.summercode.nettest.domain.model

data class SpeedSample(
    val currentMbps: Double,
    val totalBytes: Long,
    val elapsedMillis: Long
)
