package com.example.multicloud.navigation

sealed class NavRoutes(val route: String, val label: String) {
    object Providers : NavRoutes("providers", "云盘")
    object Accounts : NavRoutes("accounts", "账户")
    object Files : NavRoutes("files", "文件")
    object Transfers : NavRoutes("transfers", "传输")
    object Settings : NavRoutes("settings", "设置")
    object Auth : NavRoutes("auth", "登录")
    object Login : NavRoutes("login", "登录")
}