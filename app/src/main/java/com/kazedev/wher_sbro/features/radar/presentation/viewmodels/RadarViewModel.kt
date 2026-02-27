package com.kazedev.wher_sbro.features.radar.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kazedev.wher_sbro.core.network.TokenManager
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CoordinatesDto
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.WsMessageDto
import com.kazedev.wher_sbro.features.radar.domain.location.LocationTracker
import com.kazedev.wher_sbro.features.radar.domain.repositories.RadarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.*

@HiltViewModel
class RadarViewModel @Inject constructor(
    private val radarRepository: RadarRepository,
    private val locationTracker: LocationTracker,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RadarUiState())
    val uiState: StateFlow<RadarUiState> = _uiState.asStateFlow()

    private var isTrackingInitialized = false

    fun initTracking(roomCode: String, targetName: String) {
        if (isTrackingInitialized) return
        isTrackingInitialized = true

        // Forzamos el estado de "esperando" al iniciar, sin importar qué nombre nos pasen
        _uiState.update { it.copy(targetName = "Esperando señal...", isFriendConnected = false) }

        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.update { it.copy(error = "Sesión inválida") }
                return@launch
            }

            radarRepository.connectToRadar(roomCode, token)

            launch {
                radarRepository.getIncomingMessages().collect { message ->
                    handleIncomingMessage(message)
                }
            }

            launch {
                locationTracker.getLocationUpdates(3000L).collect { myCoords ->
                    _uiState.update { it.copy(myLat = myCoords.lat, myLon = myCoords.lon) }
                    radarRepository.sendMyLocation(myCoords)
                    recalculateRadar()
                }
            }
        }
    }

    private fun handleIncomingMessage(message: WsMessageDto) {
        when (message.event) {
            "CONNECTED" -> {
                _uiState.update { it.copy(isConnected = true, isLoading = false) }
            }
            "FRIEND_JOINED" -> { // Si tu backend avisa cuando alguien entra
                _uiState.update { it.copy(isFriendConnected = true, targetName = "AMIGO ENCONTRADO") }
            }
            // OJO: Solo escuchamos FRIEND_MOVED. Ignoramos UPDATE_LOCATION para no leer nuestro eco.
            "FRIEND_MOVED" -> {
                message.data?.let { friendCoords ->
                    _uiState.update {
                        it.copy(
                            friendLat = friendCoords.lat,
                            friendLon = friendCoords.lon,
                            isFriendConnected = true, // Sabemos que ya está aquí
                            targetName = message.message ?: "Objetivo Localizado",
                            isLoading = false
                        )
                    }
                    recalculateRadar()
                }
            }
            "ERROR" -> _uiState.update { it.copy(error = message.message) }
            "FRIEND_DISCONNECTED" -> {
                // Si el amigo se va, borramos su punto del mapa y volvemos a esperar
                _uiState.update {
                    it.copy(
                        isFriendConnected = false,
                        targetName = "Esperando compañero...",
                        friendLat = 0.0,
                        friendLon = 0.0
                    )
                }
            }
        }
    }

    private fun recalculateRadar() {
        val state = _uiState.value
        if (state.myLat == 0.0 || state.friendLat == 0.0) return // Esperando señal de ambos

        val distance = calculateDistance(state.myLat, state.myLon, state.friendLat, state.friendLon)
        val bearing = calculateBearing(state.myLat, state.myLon, state.friendLat, state.friendLon)

        _uiState.update {
            it.copy(
                distanceMeters = distance.toInt(),
                bearingDegrees = bearing.toFloat()
            )
        }
    }

    // Se llama automáticamente cuando el usuario destruye la pantalla (le da al botón Atrás o Salir)
    override fun onCleared() {
        super.onCleared()
        radarRepository.disconnect()
    }

    // --- MAGIA MATEMÁTICA ---

    // Fórmula de Haversine: Calcula la distancia en metros en una esfera (la Tierra)
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371e3 // Radio de la Tierra en metros
        val phi1 = lat1 * PI / 180
        val phi2 = lat2 * PI / 180
        val deltaPhi = (lat2 - lat1) * PI / 180
        val deltaLambda = (lon2 - lon1) * PI / 180

        val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
                cos(phi1) * cos(phi2) *
                sin(deltaLambda / 2) * sin(deltaLambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }

    // Calcula el ángulo (Bearing) para que la brújula apunte hacia tu amigo
    private fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val phi1 = lat1 * PI / 180
        val phi2 = lat2 * PI / 180
        val lambda1 = lon1 * PI / 180
        val lambda2 = lon2 * PI / 180

        val y = sin(lambda2 - lambda1) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(lambda2 - lambda1)
        val theta = atan2(y, x)

        return (theta * 180 / PI + 360) % 360 // Normalizado de 0 a 360 grados
    }
}