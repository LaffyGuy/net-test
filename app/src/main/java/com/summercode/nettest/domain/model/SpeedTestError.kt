package com.summercode.nettest.domain.model

sealed interface SpeedTestError {

    data object NoConnection: SpeedTestError

    data class ServerUnavailable(val code: Int) : SpeedTestError

    data class Unexpected(val message: String): SpeedTestError

}

class SpeedTestException(val error: SpeedTestError): Exception(error.toString())