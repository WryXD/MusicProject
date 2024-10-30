package com.example.musicproject.helper

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class ERROR(val message: String) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}