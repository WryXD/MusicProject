package com.example.musicproject.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicproject.ui.auth_screen.AuthScreen
import com.example.musicproject.ui.birthday_screen.DateOfBirthScreen
import com.example.musicproject.ui.boarding_screen.BoardingScreen
import com.example.musicproject.ui.main.MainScreen
import com.example.musicproject.ui.name_screen.NameScreen
import com.example.musicproject.ui.password_screen.PasswordScreen
import com.example.musicproject.utils.AnimationUtils
import com.example.musicproject.viewmodel.auth.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.BoardingScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            BoardingScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = Screen.AuthScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            AuthScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = Screen.PasswordScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            PasswordScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = Screen.NameScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            NameScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = Screen.BirthDayScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            DateOfBirthScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = Screen.MainScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            MainScreen()
        }
    }
}
