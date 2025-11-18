package com.example.multicloud.providers

import android.content.Context

object ProviderRegistry {
    val drivers: List<CloudProviderDriver> = listOf(
        com.example.multicloud.providers.aliyun.AliyunDriveDriver(),
        com.example.multicloud.providers.baidu.BaiduNetdiskDriver(),
        com.example.multicloud.providers.tencent.TencentCosDriver(),
        com.example.multicloud.providers.huawei.HuaweiObsDriver(),
        com.example.multicloud.providers.yun123.Yun123Driver()
    )
    fun configured(context: Context): List<CloudProviderDriver> = drivers.filter { it.isConfigured(context) }
}