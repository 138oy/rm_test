package com.example.rmtestapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactors.CamerasInteractor
import com.example.rmtestapp.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CamerasViewModel @Inject constructor(private val interactor: CamerasInteractor) :
    ViewModel() {

    private val _state = MutableStateFlow<UIState>(UIState.Loading)
    val state = _state.asStateFlow()

    internal fun getCameras() {
        viewModelScope.launch {
            try {
                _state.value = UIState.Success(interactor.getCameras())
                Log.d("CVM_SUCCESS", "${_state.value}")
            } catch (t: Throwable) {
                _state.value = UIState.Error(t.message.toString())
                Log.d("CVM_FAIL", "${t.message}")
                throw t
            }
        }
    }

    internal fun onFavoritePressed(id: Int) {
        viewModelScope.launch {
            interactor.updateFavorite(id)
        }
    }
}
