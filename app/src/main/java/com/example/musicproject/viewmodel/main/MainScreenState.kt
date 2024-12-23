package com.example.musicproject.viewmodel.main

data class MainScreenState(
    val bottomNavId: Int = 0,
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val isShowBottomSheet: Boolean = false,
    val isDrawerOpen: Boolean = false,
    val isAddLikedSong: Boolean = false,
)
