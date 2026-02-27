package com.kazedev.wher_sbro.features.radar.presentation.viewmodels

data class LobbyUiState(
    val isLoading: Boolean = false,
    val userId: Int = 0,
    val operatorName: String = "",
    val frequencyCode: String = "",
    val usersInRoom: List<String> = emptyList(),
    val roomCode: String = "",
    val error: String? = null,
    val isSessionExpired: Boolean = false
)