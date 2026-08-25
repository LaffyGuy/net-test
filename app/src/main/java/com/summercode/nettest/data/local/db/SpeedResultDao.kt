package com.summercode.nettest.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeedResultDao {

    @Query("SELECT * FROM speed_results ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<SpeedResultEntity>>

    @Insert
    suspend fun insert(result: SpeedResultEntity)

}