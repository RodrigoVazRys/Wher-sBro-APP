package com.kazedev.wher_sbro.features.auth.data.repositories

import com.kazedev.wher_sbro.core.network.AuthApiService
import com.kazedev.wher_sbro.core.network.TokenManager
import com.kazedev.wher_sbro.features.auth.data.datasources.remote.mapper.toDomain
import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.LoginRequest
import com.kazedev.wher_sbro.features.auth.domain.entities.User
import com.kazedev.wher_sbro.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val request = LoginRequest(email, pass)
            val response = apiService.login(request)

            // Guardamos el token en nuestro SSOT en memoria
            tokenManager.token = response.accessToken

            // Retornamos el modelo de Dominio
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, email: String, pass: String): Result<Boolean> {
        return Result.success(true)
    }
}