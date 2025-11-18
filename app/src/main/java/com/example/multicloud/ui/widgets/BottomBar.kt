package com.example.multicloud.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.multicloud.navigation.NavRoutes
import com.example.multicloud.providers.ProviderRegistry

@Composable
fun BottomBar(navController: NavHostController) {
    val backStack = navController.currentBackStackEntryAsState()
    val current = backStack.value?.destination?.route
    val items = listOf(NavRoutes.Providers, NavRoutes.Files, NavRoutes.Transfers, NavRoutes.Settings)
    NavigationBar {
        items.forEach { route ->
            val selected = current?.startsWith(route.route) == true
            val icon = when (route) {
                NavRoutes.Providers -> Icons.Default.Cloud
                NavRoutes.Files -> Icons.Default.Folder
                NavRoutes.Transfers -> Icons.Default.SwapHoriz
                else -> Icons.Default.Settings
            }
            NavigationBarItem(selected = selected, onClick = {
                val first = ProviderRegistry.drivers.firstOrNull()?.id ?: "aliyundrive"
                val target = if (route == NavRoutes.Files) NavRoutes.Files.route + "/" + first else route.route
                navController.navigate(target) { popUpTo(NavRoutes.Providers.route) { inclusive = false } }
            }, icon = { Icon(icon, contentDescription = route.label) }, label = { Text(route.label) })
        }
    }
}