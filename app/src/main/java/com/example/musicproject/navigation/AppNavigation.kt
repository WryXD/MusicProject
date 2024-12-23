package com.example.musicproject.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicproject.ui.auth_screen.AuthScreen
import com.example.musicproject.ui.birthday_screen.DateOfBirthScreen
import com.example.musicproject.ui.boarding_screen.BoardingScreen
import com.example.musicproject.ui.gender.GenderScreen
import com.example.musicproject.ui.main.MainScreen
import com.example.musicproject.ui.main.home_screen.HomeScreen
import com.example.musicproject.ui.name_screen.NameScreen
import com.example.musicproject.ui.password_screen.PasswordScreen
import com.example.musicproject.utils.AnimationUtils
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.birthday.DateOfBirthViewModel
import com.example.musicproject.viewmodel.gender.GenderViewModel
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.home.HomeViewModel
import com.example.musicproject.viewmodel.main.library.LibraryViewModel
import com.example.musicproject.viewmodel.main.search.SearchViewModel
import com.example.musicproject.viewmodel.name.NameViewModel
import com.example.musicproject.viewmodel.password.PasswordViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    authViewModel: AuthViewModel = hiltViewModel(),
    passwordViewModel: PasswordViewModel = hiltViewModel(),
    nameViewModel: NameViewModel = hiltViewModel(),
    dobViewModel: DateOfBirthViewModel = hiltViewModel(),
    genderViewModel: GenderViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    libraryViewModel: LibraryViewModel = hiltViewModel(),
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
            PasswordScreen(
                navController = navController,
                authViewModel = authViewModel,
                passwordViewModel = passwordViewModel
            )
        }

        composable(
            route = Screen.NameScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            NameScreen(
                navController = navController,
                authViewModel = authViewModel,
                nameViewModel = nameViewModel
            )
        }

        composable(
            route = Screen.BirthDayScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            DateOfBirthScreen(
                navController = navController,
                authViewModel = authViewModel,
                dobViewModel = dobViewModel
            )
        }

        composable(
            route = Screen.MainScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            MainScreen(
                navController = navController,
                authViewModel = authViewModel,
                mainViewModel = mainViewModel,
                homeViewModel = homeViewModel,
                searchViewModel = searchViewModel,
                passwordViewModel = passwordViewModel,
                libraryViewModel = libraryViewModel
            )
        }

        composable(
            route = Screen.GenderScreen.route,
            enterTransition = AnimationUtils.slideIntoLeft(),
            popExitTransition = AnimationUtils.slideOutOfLeft(),
        ) {
            GenderScreen(
                navController = navController,
                authViewModel = authViewModel,
                genderViewModel = genderViewModel
            )
        }

        composable(
            route = Screen.HomeScreen.route,
        ) {
            HomeScreen(
                mainViewModel = mainViewModel,
                homeViewModel = homeViewModel,
                libraryViewModel = libraryViewModel
            )
        }
    }
}
