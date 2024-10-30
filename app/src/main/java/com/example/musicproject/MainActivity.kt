package com.example.musicproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.musicproject.navigation.AppNavigation
import com.example.musicproject.navigation.Screen
import com.example.musicproject.ui.theme.MusicProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicProjectTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    startDestination = Screen.BoardingScreen.route
                )
            }
        }
    }
}
