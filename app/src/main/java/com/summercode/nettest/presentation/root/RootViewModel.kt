package com.summercode.nettest.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summercode.nettest.domain.model.AppMode
import com.summercode.nettest.domain.model.AppModeResult
import com.summercode.nettest.domain.usecase.ResolveAppModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RootViewModel(
    private val resolveAppMode: ResolveAppModeUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<RootUiState>(RootUiState.Loading)
    val uiState: StateFlow<RootUiState> = _uiState.asStateFlow()

    init {
        resolveMode()
    }

    fun retry() {
        if (_uiState.value is RootUiState.Loading) return
        resolveMode()
    }

    private fun resolveMode() {
        _uiState.value = RootUiState.Loading

        viewModelScope.launch {
            _uiState.value = when (val result = resolveAppMode()) {
                is AppModeResult.Success -> RootUiState.Ready(result.mode)
                is AppModeResult.Failure -> RootUiState.Error(result.error)
            }
        }
    }

}