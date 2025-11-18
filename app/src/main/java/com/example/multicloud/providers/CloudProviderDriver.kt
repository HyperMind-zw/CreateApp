package com.example.multicloud.providers

import android.content.Context
import android.net.Uri
import com.example.multicloud.model.CloudFile
import com.example.multicloud.model.Transfer

interface CloudProviderDriver {
    val id: String
    val displayName: String
    fun requiredCredentials(): List<String>
    fun isConfigured(context: Context): Boolean
    fun saveCredentials(context: Context, values: Map<String, String>)
    fun listRootFiles(context: Context): List<CloudFile>
    fun listFiles(context: Context, path: String): List<CloudFile>
    fun upload(context: Context, localUri: Uri, remotePath: String): Transfer
    fun download(context: Context, fileId: String, localUri: Uri): Transfer
}