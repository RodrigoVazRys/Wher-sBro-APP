package com.kazedev.wher_sbro.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazedev.wher_sbro.features.auth.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(value: String) { _email.value = value }
    fun onPasswordChanged(value: String) { _password.value = value }

    fun onRegister() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _uiState.value = RegisterUiState.Error("Los campos no pueden estar vacíos")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            val result = registerUseCase(_email.value, _password.value)

            result.fold(
                onSuccess = { isSuccess ->
                    if (isSuccess) {
                        _uiState.value = RegisterUiState.Success
                    } else {
                        _uiState.value = RegisterUiState.Error("No se pudo completar el registro")
                    }
                },
                onFailure = { error ->
                    _uiState.value = RegisterUiState.Error(error.message ?: "Error desconocido en el registro")
                }
            )
        }
    }
}

// Actualiza tus estados para que Success no pida un username obligatoriamente
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState() // <-- Cambiado de data class a object
    data class Error(val message: String) : RegisterUiState()
}