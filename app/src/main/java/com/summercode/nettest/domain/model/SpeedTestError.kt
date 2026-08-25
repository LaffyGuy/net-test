package com.summercode.nettest.domain.model

interface SpeedTestError {

    data object NoConnection: SpeedTestError
    data class Unexpected(val message: String): SpeedTestError

}

class SpeedTestException(val error: SpeedTestError): Exception(error.toString())