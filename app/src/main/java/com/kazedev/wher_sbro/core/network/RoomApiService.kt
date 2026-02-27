package com.kazedev.wher_sbro.core.network

import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CreateRoomResponse
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.JoinRoomResponse
import retrofit2.http.POST

interface RoomApiService {

    @POST("room/create")
    suspend fun createRoom(): CreateRoomResponse

    @POST("room/join")
    suspend fun joinRoom(code: String): JoinRoomResponse
}

