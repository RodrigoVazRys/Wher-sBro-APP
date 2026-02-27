package com.kazedev.wher_sbro.features.radar.domain.repositories

import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CoordinatesDto
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.WsMessageDto
import kotlinx.coroutines.flow.Flow

interface RadarRepository {
    fun connectToRadar(roomCode: String, token: String)

    fun getIncomingMessages(): Flow<WsMessageDto>

    fun sendMyLocation(coordinates: CoordinatesDto)

    fun disconnect()
}