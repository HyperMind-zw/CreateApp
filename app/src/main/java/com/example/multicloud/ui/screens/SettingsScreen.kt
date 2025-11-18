package com.example.multicloud.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multicloud.vm.AppController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun SettingsScreen(controller: AppController, paddingValues: PaddingValues) {
    val darkTheme = remember { mutableStateOf(false) }
    val autoSync = remember { mutableStateOf(true) }
    val notifications = remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        TopAppBar(title = { Text("设置") })
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            SettingItem(
                title = "深色模式",
                description = "切换应用的深色主题"
            ) {
                Switch(
                    checked = darkTheme.value,
                    onCheckedChange = { darkTheme.value = it }
                )
            }
            
            SettingItem(
                title = "自动同步",
                description = "在连接网络时自动同步文件"
            ) {
                Switch(
                    checked = autoSync.value,
                    onCheckedChange = { autoSync.value = it }
                )
            }
            
            SettingItem(
                title = "通知",
                description = "接收传输完成和错误通知"
            ) {
                Switch(
                    checked = notifications.value,
                    onCheckedChange = { notifications.value = it }
                )
            }
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "关于",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "多云盘管理器 v1.0.0",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "一个统一管理多个云存储服务的应用",
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            content()
        }
    }
}