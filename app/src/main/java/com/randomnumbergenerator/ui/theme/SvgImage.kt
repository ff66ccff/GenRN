package com.randomnumbergenerator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun SvgImage(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(0.0f, 0.0f)
            // 这里添加SVG路径的移动和绘制命令
        }
        drawPath(path = path, color = Color.Black) // 根据需要修改颜色
    }
}
