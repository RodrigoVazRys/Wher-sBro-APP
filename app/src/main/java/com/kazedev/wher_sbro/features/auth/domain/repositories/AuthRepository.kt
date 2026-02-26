package com.kazedev.wher_sbro.features.auth.domain.repositories

import com.kazedev.wher_sbro.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<User>

    suspend fun register(email: String, pass: String): Result<Boolean>
}