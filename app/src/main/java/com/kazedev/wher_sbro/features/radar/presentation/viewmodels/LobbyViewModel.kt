package com.kazedev.wher_sbro.features.radar.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazedev.wher_sbro.features.radar.domain.repositories.RoomRepository
import com.kazedev.wher_sbro.features.radar.presentation.viewmodels.LobbyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    fun onFrequencyCodeChange(code: String) {
        _uiState.update { it.copy(frequencyCode = code) }
    }

    fun createRoom() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            roomRepository.createRoom()
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            roomCode = response.roomCode
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
        }
    }

    fun joinRoom(userId: Int, username: String) {
        val code = _uiState.value.frequencyCode
        if (code.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa un código de sala") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            roomRepository.joinRoom(code, userId, username)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            roomCode = response.roomCode,
                            usersInRoom = response.usersInRoom
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
        }
    }
}