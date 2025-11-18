package com.example.multicloud.data

import com.example.multicloud.model.CloudAccount
import com.example.multicloud.model.CloudFile
import com.example.multicloud.model.CloudProvider
import com.example.multicloud.model.Transfer
import com.example.multicloud.model.TransferStatus

class FakeRepository {
    private val providers = listOf(
        CloudProvider("gdrive", "Google Drive"),
        CloudProvider("dropbox", "Dropbox"),
        CloudProvider("onedrive", "OneDrive"),
        CloudProvider("box", "Box"),
        CloudProvider("webdav", "WebDAV")
    )
    private val accounts = mutableListOf(
        CloudAccount("acc1", "gdrive", "个人账号", true),
        CloudAccount("acc2", "dropbox", "工作账号", false)
    )
    private val files = mutableMapOf<String, List<CloudFile>>()
    private val transfers = mutableListOf<Transfer>()

    init {
        files["gdrive"] = listOf(
            CloudFile("f1", "gdrive", "文档", "/文档", true, 0),
            CloudFile("f2", "gdrive", "照片.jpg", "/照片.jpg", false, 1_024_000)
        )
        files["dropbox"] = listOf(
            CloudFile("f3", "dropbox", "项目", "/项目", true, 0),
            CloudFile("f4", "dropbox", "报告.pdf", "/报告.pdf", false, 2_048_000)
        )
        transfers.add(Transfer("t1", "照片.jpg", 0.6f, TransferStatus.Running))
    }

    fun listProviders(): List<CloudProvider> = providers
    fun listAccounts(): List<CloudAccount> = accounts
    fun toggleAccountConnection(id: String) {
        accounts.replaceAll { if (it.id == id) it.copy(connected = !it.connected) else it }
    }
    fun listFiles(providerId: String): List<CloudFile> = files[providerId] ?: emptyList()
    fun listTransfers(): List<Transfer> = transfers
}