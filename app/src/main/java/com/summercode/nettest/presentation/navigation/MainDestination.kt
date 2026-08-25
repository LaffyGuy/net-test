package com.summercode.nettest.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.summercode.nettest.R

enum class MainDestination(
    val route: Any,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    Test(
        route = TestRoute,
        labelRes = R.string.tab_test,
        icon = Icons.Filled.PlayArrow
    ),
    Statistics(
        route = StatisticsRoute,
        labelRes = R.string.tab_statistics,
        icon = Icons.AutoMirrored.Filled.List
    )
}