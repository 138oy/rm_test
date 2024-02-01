package com.example.rmtestapp.ui

sealed class UIState {
    object Loading : UIState()

    data class Error(val errorMsg: String) : UIState()
    data class Success<T>(val data: T) : UIState()
}
