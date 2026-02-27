package com.kazedev.wher_sbro.features.radar.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazedev.wher_sbro.core.network.TokenManager
import com.kazedev.wher_sbro.features.radar.domain.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val token = tokenManager.getToken()

            if (token == null) {
                _uiState.update { it.copy(isSessionExpired = true) }
            } else {
                launch {
                    tokenManager.usernameFlow.collect { name ->
                        _uiState.update { it.copy(operatorName = name ?: "UNKNOWN_USER") }
                    }
                }
                launch {
                    tokenManager.userIdFlow.collect { id ->
                        _uiState.update { it.copy(userId = id ?: 0) }
                    }
                }
            }
        }
    }

    fun onFrequencyCodeChange(code: String) {
        _uiState.update { it.copy(frequencyCode = code) }
    }

    fun createRoom() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                roomRepository.createRoom()
                    .onSuccess { response ->
                        _uiState.update { it.copy(isLoading = false, roomCode = response.roomCode) }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message) }
                    }
            } catch (e: Exception) {
                Log.e("LobbyVM", "Exception: ${e.message}", e)
            }
        }
    }

    fun joinRoom() {
        val code = _uiState.value.frequencyCode
        val userId = _uiState.value.userId
        val username = _uiState.value.operatorName

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

    fun clearRoomCode() {
        _uiState.update { it.copy(roomCode = "") }
    }
}