package com.eva.firebasequizapp.core.util

sealed class Resource<T>(val value: T?, val message: String? = null) {
    class Success<T>(value: T) : Resource<T>(value, message = null)
    class Loading<T>() : Resource<T>(value = null, message = null)
    class Error<T>(message: String) : Resource<T>(value = null, message = message)
}