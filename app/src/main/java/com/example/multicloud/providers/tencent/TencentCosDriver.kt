package com.example.multicloud.providers.tencent

import android.content.Context
import android.net.Uri
import com.example.multicloud.auth.CredentialStore
import com.example.multicloud.model.CloudFile
import com.example.multicloud.model.Transfer
import com.example.multicloud.model.TransferStatus
import com.example.multicloud.providers.CloudProviderDriver

class TencentCosDriver : CloudProviderDriver {
    override val id = "tencentcos"
    override val displayName = "腾讯云 COS"
    override fun requiredCredentials(): List<String> = listOf("secret_id", "secret_key", "bucket", "region")
    override fun isConfigured(context: Context): Boolean = CredentialStore(context).has(id)
    override fun saveCredentials(context: Context, values: Map<String, String>) { CredentialStore(context).put(id, values) }
    override fun listRootFiles(context: Context): List<CloudFile> = listFiles(context, "")
    override fun listFiles(context: Context, path: String): List<CloudFile> = emptyList()
    override fun upload(context: Context, localUri: Uri, remotePath: String): Transfer = Transfer("cos-upload", remotePath, 0f, TransferStatus.Failed)
    override fun download(context: Context, fileId: String, localUri: Uri): Transfer = Transfer("cos-download", fileId, 0f, TransferStatus.Failed)
}