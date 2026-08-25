package com.summercode.nettest.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.summercode.nettest.domain.model.ConfigError
import com.summercode.nettest.presentation.common.ErrorContent
import com.summercode.nettest.presentation.common.LoadingContent
import com.summercode.nettest.presentation.main.MainScreen
import com.summercode.nettest.ui.theme.NetTestTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootScreen(
    modifier: Modifier = Modifier,
    viewModel: RootViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RootContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@Composable
fun RootContent(
    uiState: RootUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is RootUiState.Loading -> LoadingContent(modifier = modifier)

        is RootUiState.Error -> {
            ErrorContent(
                message = stringResource(uiState.error.toMessageRes()),
                onRetry = onRetry,
                modifier = modifier,
            )
        }

        is RootUiState.Ready -> {
            MainScreen(
                mode = uiState.mode,
                modifier = modifier,
            )
        }
    }
}

@Preview(name = "Loading", showBackground = true)
@Composable
private fun RootContentLoadingPreview() {
    NetTestTheme {
        RootContent(uiState = RootUiState.Loading, onRetry = {})
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun RootContentErrorPreview() {
    NetTestTheme {
        RootContent(
            uiState = RootUiState.Error(ConfigError.NoConnection),
            onRetry = {},
        )
    }
}