package com.example.rmtestapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactors.DoorsInteractor
import com.example.rmtestapp.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoorsViewModel @Inject constructor(private val interactor: DoorsInteractor) :
    ViewModel() {

    private val _state = MutableStateFlow<UIState>(UIState.Loading)
    val state = _state.asStateFlow()

    internal fun getDoors() {
        viewModelScope.launch {
            try {
                _state.value = UIState.Success(interactor.getDoors())
                Log.d("DVM_SUCCESS", "${_state.value}")
            } catch (t: Throwable) {
                _state.value = UIState.Error(t.message.toString())
                Log.d("DVM_FAIL", "${t.message}")
                throw t
            }
        }
    }

    internal fun onFavoritePressed(id: Int) {
        interactor.updateFavorite(id)
    }

    internal fun onNameChange(id: Int, name: String) {
        interactor.updateName(id, name)
    }
}
