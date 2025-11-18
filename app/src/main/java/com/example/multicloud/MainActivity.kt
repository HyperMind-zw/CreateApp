package com.example.multicloud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multicloud.navigation.NavRoutes
import com.example.multicloud.ui.screens.AccountScreen
import com.example.multicloud.ui.screens.FileBrowserScreen
import com.example.multicloud.ui.screens.ProviderListScreen
import com.example.multicloud.ui.screens.SettingsScreen
import com.example.multicloud.ui.screens.TransfersScreen
import com.example.multicloud.ui.screens.AuthScreen
import com.example.multicloud.ui.screens.LoginScreen
import com.example.multicloud.ui.theme.MultiCloudTheme
import com.example.multicloud.vm.AppController
import com.example.multicloud.vm.rememberAppController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MultiCloudApp() }
    }
}

@Composable
fun MultiCloudApp() {
    MultiCloudTheme {
        val navController = rememberNavController()
        val controller = rememberAppController()
        Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
            com.example.multicloud.ui.widgets.BottomBar(navController = navController)
        }) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = if (controller.authed) NavRoutes.Providers.route else NavRoutes.Login.route
            ) {
                composable(NavRoutes.Login.route) {
                    LoginScreen(controller = controller, paddingValues = paddingValues, onSuccess = {
                        navController.navigate(NavRoutes.Providers.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } }
                    })
                }
                composable(NavRoutes.Providers.route) {
                    ProviderListScreen(controller = controller, paddingValues = paddingValues, onOpenAccounts = {
                        navController.navigate(NavRoutes.Accounts.route)
                    }, onOpenAuth = {
                        navController.navigate(NavRoutes.Auth.route)
                    }, onOpenFiles = { providerId ->
                        navController.navigate(NavRoutes.Files.route + "/" + providerId)
                    })
                }
                composable(NavRoutes.Accounts.route) {
                    AccountScreen(controller = controller, paddingValues = paddingValues, onOpenAuth = {
                        navController.navigate(NavRoutes.Auth.route)
                    })
                }
                composable(NavRoutes.Auth.route) {
                    AuthScreen(paddingValues = paddingValues)
                }
                composable(NavRoutes.Files.route + "/{providerId}") { backStackEntry ->
                    val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
                    FileBrowserScreen(controller = controller, paddingValues = paddingValues, providerId = providerId)
                }
                composable(NavRoutes.Transfers.route) {
                    TransfersScreen(controller = controller, paddingValues = paddingValues)
                }
                composable(NavRoutes.Settings.route) {
                    SettingsScreen(controller = controller, paddingValues = paddingValues)
                }
            }
        }
    }
}