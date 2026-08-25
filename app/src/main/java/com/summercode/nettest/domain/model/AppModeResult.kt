package com.summercode.nettest.domain.model

sealed interface AppModeResult {

    data class Success(val mode: AppMode): AppModeResult

    data class Failure(val error: ConfigError): AppModeResult

}