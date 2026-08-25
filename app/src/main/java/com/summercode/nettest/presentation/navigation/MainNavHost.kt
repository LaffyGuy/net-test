package com.summercode.nettest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.summercode.nettest.domain.model.AppMode
import com.summercode.nettest.presentation.placeholder.ModeUnavailableContent
import com.summercode.nettest.presentation.speed.SpeedTestScreen
import com.summercode.nettest.presentation.stats.StatisticsScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    mode: AppMode,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TestRoute,
        modifier = modifier,
    ) {
        composable<TestRoute> {
            when (mode) {
                AppMode.SPEED -> SpeedTestScreen()
                AppMode.PING -> ModeUnavailableContent()
            }
        }
        composable<StatisticsRoute> {
            StatisticsScreen()
        }
    }
}