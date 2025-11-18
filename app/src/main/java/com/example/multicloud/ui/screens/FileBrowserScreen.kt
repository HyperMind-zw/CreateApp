package com.example.multicloud.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multicloud.vm.AppController
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.multicloud.providers.ProviderRegistry
import com.example.multicloud.model.CloudFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.ui.Alignment

@Composable
fun FileBrowserScreen(controller: AppController, paddingValues: PaddingValues, providerId: String) {
    val context = LocalContext.current
    var list by remember { mutableStateOf<List<CloudFile>?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var selectedFile by remember { mutableStateOf<CloudFile?>(null) }
    
    // 页面加载时获取文件列表
    if (list == null) {
        controller.listFilesRemote(providerId, "/") { result ->
            try {
                list = result
                loading = false
            } catch (e: Exception) {
                error = e.message ?: "加载文件列表失败"
                loading = false
            }
        }
    }
    
    val driver = ProviderRegistry.drivers.find { it.id == providerId }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null && driver != null) {
            try {
                // 调用驱动上传文件
                driver.upload(context, uri, "/")
            } catch (e: Exception) {
                error = "上传失败: ${e.message}"
            }
        }
    }
    
    if (showDownloadDialog && selectedFile != null) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = { Text("下载文件") },
            text = { Text("确定要下载文件 ${selectedFile?.name} 吗？") },
            confirmButton = {
                TextButton(onClick = {
                    // 实际的下载逻辑应该在这里实现
                    // 根据后端架构，应该通过后端代理下载文件
                    showDownloadDialog = false
                }) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDownloadDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
    
    LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { TopAppBar(title = { Text("文件 · " + providerId) }) }
        item { 
            Button(onClick = { picker.launch(arrayOf("*/*")) }) { 
                Text("上传文件") 
            } 
        }
        
        if (loading) {
            item {
                Text("加载中...")
            }
        } else if (error != null) {
            item {
                Text("错误: $error")
            }
        } else {
            list?.let { fileList ->
                if (fileList.isEmpty()) {
                    item {
                        Text("该目录为空")
                    }
                } else {
                    items(fileList) { f ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = (if (f.isFolder) "📁 " else "📄 ") + f.name,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = f.sizeBytes.toString() + "B",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                
                                if (!f.isFolder) {
                                    Button(
                                        onClick = {
                                            selectedFile = f
                                            showDownloadDialog = true
                                        },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) { 
                                        Text("下载") 
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}