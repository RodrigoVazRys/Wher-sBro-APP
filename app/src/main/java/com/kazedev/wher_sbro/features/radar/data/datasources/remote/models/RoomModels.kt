package com.kazedev.wher_sbro.features.radar.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class CreateRoomResponse(
    @SerializedName("code") val roomCode: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("message") val message: String,
)

data class JoinRoomRequest(
    @SerializedName("code") val roomCode: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("username") val username: String,
)

data class JoinRoomResponse(
    @SerializedName("code") val roomCode: String,
    @SerializedName("message") val message: String,
    @SerializedName("users_in_room") val usersInRoom: List<String>,
)