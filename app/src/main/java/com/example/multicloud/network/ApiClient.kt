package com.example.multicloud.network

import android.content.Context
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.example.com/"
    fun retrofit(context: Context): Retrofit {
        val moshi = Moshi.Builder().build()
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val auth = AuthInterceptor(context)
        val client = OkHttpClient.Builder().addInterceptor(auth).addInterceptor(logging).build()
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).build()
    }
}