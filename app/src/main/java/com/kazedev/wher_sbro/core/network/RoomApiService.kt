package com.kazedev.wher_sbro.core.network

import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CreateRoomResponse
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.JoinRoomRequest
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.JoinRoomResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomApiService {

    @POST("room/create")
    suspend fun createRoom(): CreateRoomResponse

    @POST("rooms/{code}/join")
    suspend fun joinRoom(
        @Path("code") code: String,
        @Body request: JoinRoomRequest
    ): JoinRoomResponse
}