package com.summercode.nettest.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speed_results")
data class SpeedResultEntity(
     @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "timestamp")
    val timestampMillis: Long,
    @ColumnInfo(name = "average_mbps")
    val averageMbps: Double,
    @ColumnInfo(name = "peak_mbps")
    val peakMbps: Double
)
