package com.example.multicloud.auth

import android.content.Context

class CredentialStore(private val context: Context) {
    private val prefs = context.getSharedPreferences("mc_credentials", Context.MODE_PRIVATE)
    fun get(providerId: String): Map<String, String>? {
        val raw = prefs.getString(providerId, null) ?: return null
        val pairs = raw.split("\n").mapNotNull {
            val idx = it.indexOf('=')
            if (idx <= 0) null else it.substring(0, idx) to it.substring(idx + 1)
        }
        return if (pairs.isEmpty()) null else pairs.toMap()
    }
    fun put(providerId: String, values: Map<String, String>) {
        val raw = values.entries.joinToString("\n") { it.key + "=" + it.value }
        prefs.edit().putString(providerId, raw).apply()
    }
    fun has(providerId: String): Boolean = prefs.contains(providerId)
}