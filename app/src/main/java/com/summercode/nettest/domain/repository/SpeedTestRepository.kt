package com.summercode.nettest.domain.repository

import com.summercode.nettest.domain.model.SpeedSample
import kotlinx.coroutines.flow.Flow

interface SpeedTestRepository {

    fun measure(durationMillis: Long): Flow<SpeedSample>

}