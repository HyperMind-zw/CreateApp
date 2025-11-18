package com.example.multicloud.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multicloud.vm.AppController
import androidx.compose.material3.Divider
import androidx.compose.ui.platform.LocalContext
import com.example.multicloud.providers.ProviderRegistry
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ProviderListScreen(
    controller: AppController,
    paddingValues: PaddingValues,
    onOpenAccounts: () -> Unit,
    onOpenAuth: () -> Unit,
    onOpenFiles: (String) -> Unit
) {
    val context = LocalContext.current
    val providersState = controller.providers
    controller.fetchProviders()
    
    var showAuthDialog by remember { mutableStateOf(false) }
    var selectedProviderId by remember { mutableStateOf<String?>(null) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            TopAppBar(title = { Text("多云盘") }, actions = {
                Button(onClick = onOpenAccounts) { Text("账户") }
                Button(onClick = onOpenAuth) { Text("登录") }
            })
        }
        items(providersState) { provider ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = provider.name, style = MaterialTheme.typography.titleMedium)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onOpenFiles(provider.id) },
                            colors = ButtonDefaults.buttonColors()
                        ) { Text("浏览文件") }
                        
                        Button(
                            onClick = { 
                                selectedProviderId = provider.id
                                showAuthDialog = true
                            }
                        ) { Text("去授权") }
                    }
                }
            }
        }
    }
    
    // 授权确认对话框
    if (showAuthDialog && selectedProviderId != null) {
        AlertDialog(
            onDismissRequest = { showAuthDialog = false },
            title = { Text("云存储授权") },
            text = { Text("即将跳转到云存储提供商进行授权，授权后可以管理您的文件。") },
            confirmButton = {
                TextButton(onClick = {
                    selectedProviderId?.let { providerId ->
                        com.example.multicloud.vm.OAuthBridge.start(context, providerId)
                    }
                    showAuthDialog = false
                }) {
                    Text("继续")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAuthDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}