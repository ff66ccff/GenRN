package com.randomnumbergenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // 使用 MaterialTheme 作为默认主题
                Surface(color = MaterialTheme.colorScheme.background) {
                    RandomScreen() // 使用 RandomScreen 作为 UI
                }
            }
        }
    }
}
