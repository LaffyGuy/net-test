package com.summercode.nettest.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppConfigDto(
    val mode: String? = null
)
