package com.example.musicproject.repositories.auth.remote

import android.util.Log
import com.example.musicproject.domain.model.user.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            // Sign in with email and password
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            // Get the user from the result
            val user =
                result.user ?: return Result.failure(IllegalStateException("User login failed"))
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
            val emailVerified = user.isEmailVerified
            if (!emailVerified) {
                user.sendEmailVerification().await()
                Log.e("Authentication", "Email verification sent")
            }

            // Create user profile in Firestore
            val userProfile = UserProfile(
                id = user.uid,
                name = name,
                birthday = birthday,
                gender = gender,
                email = email
            )

            try {

                if(user.isEmailVerified){
                    // Save user profile to Firestore
                    fireStore.collection("users").document(user.uid)
                        .set(userProfile)
                        .await()
                }
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

    override suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val userId = firebaseAuth.currentUser?.uid!!
            val documentSnapshot = fireStore.collection("users").document(userId).get().await()
            val userProfile = UserProfile(
                id = documentSnapshot.getString("id") ?: "",
                name = documentSnapshot.getString("name") ?: "Unknown",
                birthday = documentSnapshot.getString("birthday") ?: "N/A",
                gender = documentSnapshot.getString("gender") ?: "N/A",
                email = documentSnapshot.getString("email") ?: "N/A",
            )
            Log.e("Authentication", "Get User Profile Success!: $userProfile")
            Result.success(userProfile)
        } catch (
            e: Exception,
        ) {
            Log.e("Authentication", "Failed to get user profile: ${e.message}", e)
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