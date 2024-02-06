package com.example.common

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val value: T) : ResponseWrapper<T>()
    data class Error(val code: String? = null) :
        ResponseWrapper<Nothing>()
}
