package com.kazedev.wher_sbro.core.network

import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.LoginRequest
import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.TokenResponse
import com.kazedev.wher_sbro.features.auth.data.datasources.remote.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): UserResponse
}