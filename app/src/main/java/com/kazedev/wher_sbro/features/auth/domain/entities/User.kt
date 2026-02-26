package com.kazedev.wher_sbro.features.auth.domain.entities

data class User(
    val userId: Int,
    val username: String,
    val accessToken: String,
)