package com.example.multicloud

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.multicloud.vm.OAuthBridge

class OAuthCallbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Uri? = intent?.data
        if (data != null) {
            OAuthBridge.handleCallback(this, data)
        }
        finish()
    }
}