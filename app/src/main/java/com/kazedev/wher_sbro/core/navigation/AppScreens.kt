package com.kazedev.wher_sbro.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object RadarLobbyRoute

@Serializable
data class RadarRoute(
    val roomCode: String,
    val targetName: String
)