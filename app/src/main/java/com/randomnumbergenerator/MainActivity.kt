package com.randomnumbergenerator // 确保这是你的包名

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window // 导入 Window 类
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- 关键改动：移除原生标题栏 ---
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide() // 对于 AppCompatActivity，也隐藏 Action Bar

        val webView = WebView(this).apply {
            // 基本设置
            settings.javaScriptEnabled = true

            // 启用 DOM Storage (用于 HTML 中的 localStorage)
            settings.domStorageEnabled = true

            // (可选) 启用数据库存储 (如果未来可能用到)
            // settings.databaseEnabled = true

            // (可选) 为了更好的兼容性和调试，可以添加以下设置
            // 允许通过 file:// 访问其他 file:// (通常对于单页面应用不是必须的，但有时有帮助)
            settings.allowFileAccessFromFileURLs = true
            // 注意：allowUniversalAccessFromFileURLs 会降低安全性，仅在信任内容时使用
            // settings.allowUniversalAccessFromFileURLs = true

            // 允许混合内容 (如果你的 HTML 内部加载了 http 资源)
            // settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            // 加载你的 HTML 文件
            loadUrl("file:///android_asset/index.html")
        }

        // (可选) 启用 WebView 远程调试 (在开发阶段非常有用)
        WebView.setWebContentsDebuggingEnabled(true)

        setContentView(webView)
    }
}
