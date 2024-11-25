package com.example.musicproject.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.musicproject.R

sealed class BottomNavItems(
    val label: String,
    @DrawableRes
    val icon: Int,
    val selectedIcon: Color,
    val unselectedIcon: Color,
) {
    data object Home : BottomNavItems(
        label = "Home",
        icon = R.drawable.home,
        selectedIcon = Color.White,
        unselectedIcon = Color.DarkGray
    )

    data object Search : BottomNavItems(
        label = "Search",
        icon = R.drawable.search,
        selectedIcon = Color.White,
        unselectedIcon = Color.DarkGray
    )

    data object Library : BottomNavItems(
        label = "Library",
        icon = R.drawable.library,
        selectedIcon = Color.White,
        unselectedIcon = Color.DarkGray
    )
}
