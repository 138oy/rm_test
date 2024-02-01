package com.example.data.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): com.example.common.ResponseWrapper<T> {
    return withContext(dispatcher) {
        try {
            com.example.common.ResponseWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val code = throwable.code()
                    com.example.common.ResponseWrapper.Error(code.toString())
                }

                else -> {
                    com.example.common.ResponseWrapper.Error(throwable.message)
                }
            }
        }
    }
}
