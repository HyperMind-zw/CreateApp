package com.example.multicloud.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = context.getSharedPreferences("mc_auth", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt", null)
        val req = if (token != null) chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build() else chain.request()
        val res = chain.proceed(req)
        return res
    }
}