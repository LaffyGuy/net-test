package com.summercode.nettest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.summercode.nettest.presentation.root.RootScreen
import com.summercode.nettest.ui.theme.NetTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetTestTheme {
                RootScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}