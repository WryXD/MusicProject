package com.example.musicproject.utils

object StringUtils {
    fun isEmailValid(email: String): Boolean {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
        return isValid
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    fun isFullNameValid(name: String, surName: String): Boolean {
        return name.isNotEmpty() && surName.isNotEmpty()
    }
}