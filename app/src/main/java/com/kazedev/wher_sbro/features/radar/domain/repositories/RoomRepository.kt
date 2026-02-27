package com.kazedev.wher_sbro.features.radar.domain.repositories

import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CreateRoomResponse
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.JoinRoomResponse
import com.kazedev.wher_sbro.features.radar.domain.entities.Room

interface RoomRepository {
    suspend fun joinRoom(
        code: String,
        userId: Int,
        username: String
    ): Result<JoinRoomResponse>

    suspend fun leaveRoom( code : String )

    suspend fun createRoom (): Result<CreateRoomResponse>

}