package com.summercode.nettest.presentation.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.summercode.nettest.R
import com.summercode.nettest.domain.model.Measurement
import com.summercode.nettest.presentation.util.formatTimestamp
import com.summercode.nettest.ui.theme.NetTestTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = koinViewModel(),
) {
    val measurements by viewModel.measurements.collectAsStateWithLifecycle()

    StatisticsContent(
        measurements = measurements,
        modifier = modifier,
    )
}

@Composable
fun StatisticsContent(
    measurements: List<Measurement>,
    modifier: Modifier = Modifier,
) {
    if (measurements.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.statistics_empty),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }
        return
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = measurements,
            key = { measurement -> measurement.id },
        ) { measurement ->
            MeasurementRow(measurement = measurement)
            HorizontalDivider()
        }
    }
}

@Composable
private fun MeasurementRow(
    measurement: Measurement,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = formatTimestamp(measurement.timestampMillis),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.speed_average, measurement.averageMbps),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.speed_peak, measurement.peakMbps),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(name = "With data", showBackground = true)
@Composable
private fun StatisticsContentPreview() {
    NetTestTheme {
        StatisticsContent(
            measurements = listOf(
                Measurement(1, 1_756_000_000_000, 94.4, 102.4),
                Measurement(2, 1_755_900_000_000, 71.2, 88.6),
                Measurement(3, 1_755_800_000_000, 12.8, 15.1),
            )
        )
    }
}

@Preview(name = "Empty", showBackground = true)
@Composable
private fun StatisticsContentEmptyPreview() {
    NetTestTheme {
        StatisticsContent(measurements = emptyList())
    }
}