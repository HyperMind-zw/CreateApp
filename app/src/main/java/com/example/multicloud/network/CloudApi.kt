package com.example.multicloud.network

import com.example.multicloud.model.CloudFile
import com.example.multicloud.model.CloudProvider
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CloudApi {
    @GET("providers")
    suspend fun listProviders(): List<CloudProvider>
    @GET("accounts")
    suspend fun listAccounts(): List<com.example.multicloud.model.CloudAccount>
    @GET("cloud/{providerId}/files")
    suspend fun listFiles(@Path("providerId") providerId: String, @Query("path") path: String): List<CloudFile>
}