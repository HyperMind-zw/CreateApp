package com.example.multicloud.repository

import android.content.Context
import com.example.multicloud.model.CloudAccount
import com.example.multicloud.model.CloudProvider
import com.example.multicloud.providers.ProviderRegistry

class ProviderRepository(private val context: Context) {
    fun listProviders(): List<CloudProvider> = ProviderRegistry.drivers.map { CloudProvider(it.id, it.displayName) }
    fun listAccounts(): List<CloudAccount> = ProviderRegistry.drivers.map { CloudAccount(it.id + "-acc", it.id, it.displayName, it.isConfigured(context)) }
}