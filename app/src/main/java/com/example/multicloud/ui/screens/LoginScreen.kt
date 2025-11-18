package com.example.multicloud.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multicloud.vm.AppController

@Composable
fun LoginScreen(controller: AppController, paddingValues: PaddingValues, onSuccess: () -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    TopAppBar(title = { Text("登录") })
    androidx.compose.foundation.layout.Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(12.dp)) {
        OutlinedTextField(value = email.value, onValueChange = { email.value = it }, label = { Text("邮箱") })
        OutlinedTextField(value = password.value, onValueChange = { password.value = it }, label = { Text("密码") })
        Button(onClick = { controller.login(email.value, password.value, onSuccess, {}) }) { Text("登录") }
    }
}