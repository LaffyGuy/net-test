package com.summercode.nettest.presentation.root

import com.summercode.nettest.R
import com.summercode.nettest.domain.model.ConfigError

fun ConfigError.toMessageRes(): Int = when(this) {
    ConfigError.Malformed -> R.string.error_malformed
    ConfigError.NoConnection -> R.string.error_no_connection
    is ConfigError.ClientError -> R.string.error_client
    is ConfigError.ServerError -> R.string.error_server
    is ConfigError.Unexpected -> R.string.error_unexpected
}