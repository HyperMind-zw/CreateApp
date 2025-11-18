package com.example.multicloud.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.multicloud.providers.ProviderRegistry
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val drivers = ProviderRegistry.drivers
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { 
            TopAppBar(title = { Text("登录与授权") }) 
        }
        
        items(drivers) { d ->
            val fields = remember { mutableStateMapOf<String, String>() }
            var errorMessage by remember { mutableStateOf<String?>(null) }
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = d.displayName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    d.requiredCredentials().forEach { key ->
                        OutlinedTextField(
                            value = fields[key] ?: "", 
                            onValueChange = { fields[key] = it }, 
                            label = { Text(key) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    Button(
                        onClick = { 
                            try {
                                d.saveCredentials(context, fields)
                                scope.launch {
                                    snackbarHostState.showSnackbar("凭据保存成功")
                                }
                                errorMessage = null
                            } catch (e: Exception) {
                                errorMessage = "保存失败: ${e.message}"
                            }
                        }
                    ) { 
                        Text("保存凭证") 
                    }
                }
            }
        }
        
        item {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}