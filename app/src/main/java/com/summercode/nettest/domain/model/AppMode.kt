package com.summercode.nettest.domain.model

enum class AppMode {
    SPEED,
    PING;

    companion object {
        val Default: AppMode = SPEED

        fun fromRaw(raw: String?): AppMode {
            return entries.firstOrNull { it.name.equals(raw?.trim(), ignoreCase = true) } ?: AppMode.Default
        }
    }
}