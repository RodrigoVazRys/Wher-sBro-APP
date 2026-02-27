package com.kazedev.wher_sbro.features.radar.data.repositories

import com.kazedev.wher_sbro.core.network.RoomApiService
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CreateRoomResponse
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.JoinRoomRequest
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.JoinRoomResponse
import com.kazedev.wher_sbro.features.radar.domain.repositories.RoomRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val apiService: RoomApiService
) : RoomRepository {
    override suspend fun createRoom(): Result<CreateRoomResponse> {
            return try {
                val response = apiService.createRoom()
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        override suspend fun joinRoom(
            code: String,
            userId: Int,
            username: String
        ): Result<JoinRoomResponse> {
            return try {
                val request = JoinRoomRequest(
                    roomCode = code,
                    userId = userId,
                    username = username
                )
                val response = apiService.joinRoom(code, request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}