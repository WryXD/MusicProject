package com.example.musicproject.repositories.auth.remote

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(email: String, password: String):Result<FirebaseUser>

    suspend fun register(
        email: String, password: String, name: String,
        birthday: String,
        gender: String,
    ): Result<FirebaseUser>

    suspend fun logout()

    suspend fun checkCurrentUser(): Result<FirebaseUser>
}