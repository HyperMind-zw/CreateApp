package com.example.multicloud.vm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.app.Application
import android.content.Context
import com.example.multicloud.data.FakeRepository
import com.example.multicloud.model.CloudAccount
import com.example.multicloud.model.CloudFile
import com.example.multicloud.model.CloudProvider
import com.example.multicloud.model.Transfer
import com.example.multicloud.providers.ProviderRegistry
import com.example.multicloud.repository.ProviderRepository
import com.example.multicloud.network.ApiClient
import com.example.multicloud.network.AuthApi
import com.example.multicloud.network.CloudApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppController(private val context: Context) {
    private val fakeRepo = FakeRepository()
    private val providerRepo = ProviderRepository(context)
    private val retrofit = ApiClient.retrofit(context)
    private val authApi = retrofit.create(AuthApi::class.java)
    private val cloudApi = retrofit.create(CloudApi::class.java)
    private val scope = CoroutineScope(Dispatchers.Main)
    var providers by mutableStateOf(providerRepo.listProviders())
    var accounts by mutableStateOf(providerRepo.listAccounts())
    var transfers by mutableStateOf(fakeRepo.listTransfers())
    var authed by mutableStateOf(hasJwt())
    private fun hasJwt(): Boolean = context.getSharedPreferences("mc_auth", Context.MODE_PRIVATE).contains("jwt")
    
    fun files(providerId: String): List<CloudFile> {
        return emptyList()
    }
    
    fun refreshAccounts() { 
        accounts = providerRepo.listAccounts() 
    }
    
    fun fetchProviders() { 
        scope.launch { 
            try {
                providers = cloudApi.listProviders() 
            } catch (e: Exception) {
                // 处理错误，可能使用假数据或显示错误信息
            }
        } 
    }
    
    fun fetchAccounts() { 
        scope.launch { 
            try {
                accounts = cloudApi.listAccounts() 
            } catch (e: Exception) {
                // 处理错误，可能使用假数据或显示错误信息
            }
        } 
    }
    
    fun listFilesRemote(providerId: String, path: String, onResult: (List<CloudFile>) -> Unit) { 
        scope.launch { 
            try {
                val result = cloudApi.listFiles(providerId, path)
                onResult(result)
            } catch (e: Exception) {
                // 错误处理
                onResult(emptyList())
            }
        } 
    }
    
    fun saveTokens(access: String, refresh: String) {
        context.getSharedPreferences("mc_auth", Context.MODE_PRIVATE).edit().putString("jwt", access).putString("refresh", refresh).apply()
        authed = true
    }
    
    fun login(email: String, password: String, onDone: () -> Unit, onError: (Throwable) -> Unit) {
        scope.launch {
            try {
                val res = authApi.login(com.example.multicloud.network.LoginRequest(email, password))
                saveTokens(res.accessToken, res.refreshToken)
                onDone()
            } catch (t: Throwable) { 
                onError(t) 
            }
        }
    }
    
    // 添加登出功能
    fun logout(onDone: () -> Unit) {
        scope.launch {
            try {
                val refreshToken = context.getSharedPreferences("mc_auth", Context.MODE_PRIVATE).getString("refresh", "")
                if (refreshToken != null && refreshToken.isNotEmpty()) {
                    authApi.logout(com.example.multicloud.network.LogoutRequest(refreshToken))
                }
            } catch (e: Exception) {
                // 即使后端登出失败，也要清除本地令牌
            } finally {
                // 清除本地存储的令牌
                context.getSharedPreferences("mc_auth", Context.MODE_PRIVATE).edit().clear().apply()
                authed = false
                onDone()
            }
        }
    }
    
    // 添加刷新令牌功能
    fun refreshToken(onSuccess: (String, String) -> Unit, onError: (Throwable) -> Unit) {
        scope.launch {
            try {
                val refreshToken = context.getSharedPreferences("mc_auth", Context.MODE_PRIVATE).getString("refresh", "")
                if (refreshToken != null && refreshToken.isNotEmpty()) {
                    val res = authApi.refresh(com.example.multicloud.network.RefreshRequest(refreshToken))
                    saveTokens(res.accessToken, res.refreshToken)
                    onSuccess(res.accessToken, res.refreshToken)
                } else {
                    onError(Exception("No refresh token available"))
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}

@Composable
fun rememberAppController(): AppController = remember { AppController(androidx.compose.ui.platform.LocalContext.current) }