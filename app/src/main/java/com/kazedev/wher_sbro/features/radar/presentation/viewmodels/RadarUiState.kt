package com.kazedev.wher_sbro.features.radar.presentation.viewmodels

data class RadarUiState(
    val isLoading: Boolean = true,
    val isConnected: Boolean = false,
    val isFriendConnected: Boolean = false,
    val targetName: String = "Esperando compañero...",

    val myLat: Double = 0.0,
    val myLon: Double = 0.0,

    val friendLat: Double = 0.0,
    val friendLon: Double = 0.0,

    val distanceMeters: Int = 0,
    val bearingDegrees: Float = 0f,

    val error: String? = null
)