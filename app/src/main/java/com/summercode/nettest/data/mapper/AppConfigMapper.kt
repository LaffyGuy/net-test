package com.summercode.nettest.data.mapper

import com.summercode.nettest.data.remote.dto.AppConfigDto
import com.summercode.nettest.domain.model.AppMode

fun AppConfigDto.toAppMode(): AppMode = AppMode.fromRaw(mode)