package com.summercode.nettest.presentation.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.summercode.nettest.domain.model.AppMode
import com.summercode.nettest.domain.model.ConfigError
import com.summercode.nettest.presentation.common.ErrorContent
import com.summercode.nettest.presentation.common.LoadingContent
import com.summercode.nettest.presentation.speed.SpeedTestScreen
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
            SpeedTestScreen()
//            Column(
//                modifier = modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//            ) {
//                Text(
//                    text = uiState.mode.name,
//                    style = MaterialTheme.typography.headlineMedium,
//                )
//            }
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

@Preview(name = "Ready", showBackground = true)
@Composable
private fun RootContentReadyPreview() {
    NetTestTheme {
        RootContent(
            uiState = RootUiState.Ready(AppMode.SPEED),
            onRetry = {},
        )
    }
}