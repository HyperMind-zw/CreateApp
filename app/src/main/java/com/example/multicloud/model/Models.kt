package com.example.multicloud.model

data class CloudProvider(val id: String, val name: String)

data class CloudAccount(val id: String, val providerId: String, val displayName: String, val connected: Boolean)

data class CloudFile(
    val id: String,
    val providerId: String,
    val name: String,
    val path: String,
    val isFolder: Boolean,
    val sizeBytes: Long
)

enum class TransferStatus { Pending, Running, Completed, Failed }

data class Transfer(
    val id: String,
    val fileName: String,
    val progress: Float,
    val status: TransferStatus
)