package com.kazedev.wher_sbro.core.network

import com.google.gson.annotations.SerializedName
import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.LoginRequest
import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.TokenResponse
import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse
}