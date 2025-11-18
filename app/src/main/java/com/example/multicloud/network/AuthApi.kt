package com.example.multicloud.network

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): TokenPair
    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): TokenPair
    @POST("auth/logout")
    suspend fun logout(@Body body: LogoutRequest)
}

data class LoginRequest(val email: String, val password: String)
data class RefreshRequest(val refreshToken: String)
data class LogoutRequest(val refreshToken: String)
data class TokenPair(val accessToken: String, val refreshToken: String)