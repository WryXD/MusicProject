package com.example.musicproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.musicproject.navigation.AppNavigation
import com.example.musicproject.navigation.Screen
import com.example.musicproject.ui.theme.MusicProjectTheme
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicProjectTheme {
                val systemUiController = rememberSystemUiController()

                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = false
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = false
                )

                val navController = rememberNavController()

                val authViewModel: AuthViewModel = hiltViewModel()
                val authenticationState by authViewModel.authenticationState.collectAsState()
                when (authenticationState.isLoggedIn) {
                    true -> {
                        AppNavigation(navController, startDestination = Screen.MainScreen.route)
                    }

                    false -> {
                        AppNavigation(navController, startDestination = Screen.BoardingScreen.route)
                    }
                }
            }
        }
    }
}
