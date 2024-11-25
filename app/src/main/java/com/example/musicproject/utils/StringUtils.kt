package com.example.musicproject.utils

object StringUtils {
    fun isEmailValid(email: String): Boolean {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return isValid
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= MIN_LENGTH && password.isNotEmpty()
    }

    fun isFullNameValid(name: String, surName: String): Boolean {
        return name.isNotEmpty() && surName.isNotEmpty()
    }

    private const val  MIN_LENGTH = 8
}