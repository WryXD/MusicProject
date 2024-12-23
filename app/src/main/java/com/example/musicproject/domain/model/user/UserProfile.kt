package com.example.musicproject.domain.model.user

data class UserProfile(
    val id: String = "",
    val name: String ="",
    val birthday: String="",
    val gender: String="",
    val email: String = "",
    val profilePictureUrl: String = "",
)
