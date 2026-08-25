package com.summercode.nettest.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.summercode.nettest.domain.model.AppMode
import com.summercode.nettest.presentation.navigation.MainBottomBar
import com.summercode.nettest.presentation.navigation.MainNavHost
import com.summercode.nettest.presentation.navigation.navigateToTab
import com.summercode.nettest.presentation.navigation.toMainDestination

@Composable
fun MainScreen(
    mode: AppMode,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = backStackEntry?.destination?.toMainDestination()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                selectedDestination = selectedDestination,
                onDestinationClick = navController::navigateToTab,
            )
        },
    ) { innerPadding ->
        MainNavHost(
            navController = navController,
            mode = mode,
            modifier = Modifier.padding(innerPadding),
        )
    }
}