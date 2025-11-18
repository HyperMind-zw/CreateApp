package com.example.multicloud.providers.aliyun

import android.content.Context
import android.net.Uri
import com.example.multicloud.auth.CredentialStore
import com.example.multicloud.model.CloudFile
import com.example.multicloud.model.Transfer
import com.example.multicloud.model.TransferStatus
import com.example.multicloud.providers.CloudProviderDriver

class AliyunDriveDriver : CloudProviderDriver {
    override val id = "aliyundrive"
    override val displayName = "阿里云盘"
    override fun requiredCredentials(): List<String> = listOf("access_token")
    override fun isConfigured(context: Context): Boolean = CredentialStore(context).has(id)
    override fun saveCredentials(context: Context, values: Map<String, String>) { CredentialStore(context).put(id, values) }
    override fun listRootFiles(context: Context): List<CloudFile> = listFiles(context, "/")
    override fun listFiles(context: Context, path: String): List<CloudFile> = emptyList()
    override fun upload(context: Context, localUri: Uri, remotePath: String): Transfer = Transfer("aliyun-upload", remotePath, 0f, TransferStatus.Failed)
    override fun download(context: Context, fileId: String, localUri: Uri): Transfer = Transfer("aliyun-download", fileId, 0f, TransferStatus.Failed)
}