package com.kazedev.wher_sbro.features.radar.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class CoordinatesDto(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

data class WsMessageDto(
    @SerializedName("event") val event: String,
    @SerializedName("data") val data: CoordinatesDto?,
    @SerializedName("message") val message: String?
)