package com.summercode.nettest.presentation.speed

import androidx.annotation.StringRes
import com.summercode.nettest.R
import com.summercode.nettest.domain.model.SpeedTestError

@StringRes
fun SpeedTestError.toMessageRes(): Int = when (this) {
    is SpeedTestError.NoConnection -> R.string.error_no_connection
    is SpeedTestError.Unexpected -> R.string.error_unexpected
    is SpeedTestError.ServerUnavailable -> R.string.error_speed_server
}