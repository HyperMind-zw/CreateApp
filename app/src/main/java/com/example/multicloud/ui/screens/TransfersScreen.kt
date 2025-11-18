package com.example.multicloud.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multicloud.vm.AppController
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button

@Composable
fun TransfersScreen(controller: AppController, paddingValues: PaddingValues) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { TopAppBar(title = { Text("传输队列") }) }
        
        if (controller.transfers.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "暂无传输任务",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(controller.transfers) { t ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = t.fileName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        LinearProgressIndicator(
                            progress = t.progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = t.status.name)
                            Text(text = "${(t.progress * 100).toInt()}%")
                        }
                        
                        // 根据后端架构，应该支持暂停/继续/取消等操作
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            when (t.status) {
                                com.example.multicloud.model.TransferStatus.Running -> {
                                    Button(onClick = { /* 暂停传输 */ }) {
                                        Text("暂停")
                                    }
                                }
                                com.example.multicloud.model.TransferStatus.Pending -> {
                                    Button(onClick = { /* 取消传输 */ }) {
                                        Text("取消")
                                    }
                                }
                                com.example.multicloud.model.TransferStatus.Failed -> {
                                    Button(onClick = { /* 重试传输 */ }) {
                                        Text("重试")
                                    }
                                }
                                else -> {
                                    // Completed or other states
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}