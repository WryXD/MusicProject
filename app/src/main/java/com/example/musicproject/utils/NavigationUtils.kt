package com.example.musicproject.utils

import androidx.navigation.NavController

object NavigationUtils {
    fun navigateTo(navController: NavController, route: String, popUp: String? = null) {
        navController.navigate(route) {
            popUp?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
        }
    }

    fun navigateBack(navController: NavController) {
        navController.popBackStack()
    }
}