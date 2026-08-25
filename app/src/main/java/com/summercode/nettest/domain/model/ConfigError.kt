package com.summercode.nettest.domain.model

sealed interface ConfigError {

    data object NoConnection: ConfigError

    data class Server(val code: Int): ConfigError

    data object Malformed: ConfigError

    data class Unexpected(val message: String): ConfigError

}

class ConfigException(val error: ConfigError): Exception(error.toString())
