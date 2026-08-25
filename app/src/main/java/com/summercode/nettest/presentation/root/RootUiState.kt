package com.summercode.nettest.presentation.root

import com.summercode.nettest.domain.model.AppMode
import com.summercode.nettest.domain.model.ConfigError

sealed interface RootUiState {

    data object Loading: RootUiState

    data class Error(val error: ConfigError): RootUiState

    data class Ready(val mode: AppMode): RootUiState

}