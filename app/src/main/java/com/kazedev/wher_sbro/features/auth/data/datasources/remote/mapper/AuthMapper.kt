package com.kazedev.wher_sbro.features.auth.data.datasources.remote.mapper

import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.TokenResponse
import com.kazedev.wher_sbro.features.auth.domain.entities.User

fun TokenResponse.toDomain(): User {
    return User(
        userId = this.userId,
        username = this.username,
        accessToken = this.accessToken,
    )
}