package com.summercode.nettest.presentation.speed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.summercode.nettest.R
import com.summercode.nettest.domain.model.SpeedTestError
import com.summercode.nettest.ui.theme.NetTestTheme
import org.koin.androidx.compose.koinViewModel

private const val TEST_DURATION_MILLIS = 10_000f

@Composable
fun SpeedTestScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeedTestViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.stop()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.stop()
        }
    }

    SpeedTestContent(
        uiState = uiState,
        onStart = viewModel::start,
        onStop = viewModel::stop,
        modifier = modifier,
    )
}

@Composable
fun SpeedTestContent(
    uiState: SpeedTestUiState,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (uiState) {
            is SpeedTestUiState.Idle -> {
                Text(
                    text = stringResource(R.string.speed_idle_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }

            is SpeedTestUiState.Running -> {
                Text(
                    text = stringResource(R.string.speed_value, uiState.currentMbps),
                    style = MaterialTheme.typography.displayMedium,
                )
                LinearProgressIndicator(
                    progress = { uiState.elapsedMillis / TEST_DURATION_MILLIS },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                )
            }

            is SpeedTestUiState.Finished -> {
                Text(
                    text = stringResource(R.string.speed_average, uiState.averageMbps),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(R.string.speed_peak, uiState.peakMbps),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            is SpeedTestUiState.Failed -> Text(
                text = stringResource(uiState.error.toMessageRes()),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        val isRunning = uiState is SpeedTestUiState.Running
        Button(onClick = if (isRunning) onStop else onStart) {
            Text(
                text = stringResource(
                    if (isRunning) R.string.action_stop else R.string.action_start
                )
            )
        }
    }
}

@Preview(name = "Idle", showBackground = true)
@Composable
private fun SpeedTestIdlePreview() {
    NetTestTheme {
        SpeedTestContent(SpeedTestUiState.Idle, {}, {})
    }
}

@Preview(name = "Running", showBackground = true)
@Composable
private fun SpeedTestRunningPreview() {
    NetTestTheme {
        SpeedTestContent(
            SpeedTestUiState.Running(currentMbps = 87.4, elapsedMillis = 4200),
            {}, {},
        )
    }
}

@Preview(name = "Finished", showBackground = true)
@Composable
private fun SpeedTestFinishedPreview() {
    NetTestTheme {
        SpeedTestContent(
            SpeedTestUiState.Finished(averageMbps = 82.1, peakMbps = 104.7),
            {}, {},
        )
    }
}

@Preview(name = "Failed", showBackground = true)
@Composable
private fun SpeedTestFailedPreview() {
    NetTestTheme {
        SpeedTestContent(SpeedTestUiState.Failed(SpeedTestError.NoConnection), {}, {})
    }
}