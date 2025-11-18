package com.example.multicloud.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multicloud.vm.AppController
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun AccountScreen(controller: AppController, paddingValues: PaddingValues, onOpenAuth: () -> Unit) {
    var showDisconnectDialog by remember { mutableStateOf(false) }
    var selectedAccountId by remember { mutableStateOf<String?>(null) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { 
            TopAppBar(title = { Text("账户管理") }) 
        }
        
        if (controller.accounts.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "暂无账户配置",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(controller.accounts) { acc ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = acc.displayName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = acc.providerId,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (acc.connected) "已连接" else "未连接",
                                color = if (acc.connected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                            
                            if (acc.connected) {
                                Button(
                                    onClick = { 
                                        selectedAccountId = acc.id
                                        showDisconnectDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors()
                                ) { 
                                    Text("断开连接") 
                                }
                            } else {
                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(),
                                    enabled = false
                                ) { 
                                    Text("未配置") 
                                }
                            }
                        }
                    }
                }
            }
        }
        
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Button(
                    onClick = onOpenAuth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { 
                    Text("前往登录与授权") 
                }
            }
        }
    }
    
    // 断开连接确认对话框
    if (showDisconnectDialog) {
        AlertDialog(
            onDismissRequest = { showDisconnectDialog = false },
            title = { Text("断开账户连接") },
            text = { Text("确定要断开此云存储账户的连接吗？") },
            confirmButton = {
                TextButton(onClick = {
                    // 根据后端架构要求，应该调用后端API断开连接并删除令牌
                    showDisconnectDialog = false
                }) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDisconnectDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}