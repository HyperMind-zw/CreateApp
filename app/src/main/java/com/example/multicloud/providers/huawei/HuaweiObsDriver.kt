package com.example.multicloud.providers.huawei

import android.content.Context
import android.net.Uri
import com.example.multicloud.auth.CredentialStore
import com.example.multicloud.model.CloudFile
import com.example.multicloud.model.Transfer
import com.example.multicloud.model.TransferStatus
import com.example.multicloud.providers.CloudProviderDriver

class HuaweiObsDriver : CloudProviderDriver {
    override val id = "huaweiobs"
    override val displayName = "华为云 OBS"
    override fun requiredCredentials(): List<String> = listOf("access_key", "secret_key", "bucket", "endpoint")
    override fun isConfigured(context: Context): Boolean = CredentialStore(context).has(id)
    override fun saveCredentials(context: Context, values: Map<String, String>) { CredentialStore(context).put(id, values) }
    override fun listRootFiles(context: Context): List<CloudFile> = listFiles(context, "")
    override fun listFiles(context: Context, path: String): List<CloudFile> = emptyList()
    override fun upload(context: Context, localUri: Uri, remotePath: String): Transfer = Transfer("obs-upload", remotePath, 0f, TransferStatus.Failed)
    override fun download(context: Context, fileId: String, localUri: Uri): Transfer = Transfer("obs-download", fileId, 0f, TransferStatus.Failed)
}