package com.example.musicproject.repositories.auth.remote

import android.util.Log
import com.example.musicproject.domain.model.user.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            // Sign in with email and password
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            // Get the user from the result
            val user = result.user?: return Result.failure(IllegalStateException("User login failed"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(IllegalStateException(e))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        birthday: String,
        gender: String,
    ): Result<FirebaseUser> {
        return try {
            // Create authentication user
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user =
                result.user ?: return Result.failure(IllegalStateException("User creation failed"))

            try {
                // Create user profile in Firestore
                val userProfile = UserProfile(
                    id = user.uid,
                    name = name,
                    birthday = birthday,
                    gender = gender,
                    email = email
                )
                // Save user profile to Firestore
                fireStore.collection("users").document(user.uid)
                    .set(userProfile)
                    .await()

                Log.e("Firestore", "User profile created successfully")
                Result.success(user)

            } catch (e: Exception) {
                // If Firestore operation fails, attempt to delete the authentication user
                try {
                    user.delete().await()
                } catch (deleteError: Exception) {
                    Log.e(
                        "Firestore",
                        "Failed to delete auth user after profile creation failed",
                        deleteError
                    )
                }
                Result.failure(e)
            }
        } catch (e: Exception) {
            Log.e("Authentication", "Registration failed", e)
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun checkCurrentUser(): Result<FirebaseUser> {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            Result.success(currentUser)
        } else {
            Result.failure(IllegalStateException("User not logged in"))
        }
    }
}