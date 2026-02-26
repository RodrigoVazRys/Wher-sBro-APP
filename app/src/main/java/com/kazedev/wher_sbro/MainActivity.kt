package com.kazedev.wher_sbro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kazedev.wher_sbro.core.navigation.AppNavigation
import com.kazedev.wher_sbro.ui.theme.WhersBroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhersBroTheme {
                AppNavigation()
            }
        }
    }
}