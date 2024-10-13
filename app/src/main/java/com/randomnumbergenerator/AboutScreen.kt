package com.randomnumbergenerator.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val projectUrl = "https://github.com/ff66ccff/GenRN"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "这是一个基于Kotlin的随机数生成器，使用了Material Design。",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "官网为：$projectUrl",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(projectUrl))
                    context.startActivity(intent)
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "使用本项目生成的随机数在每个位置上都符合超几何分布，即对于单个位置，会不重复地生成完毕取值范围内的所有数字。",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "若是不打开“一次性生成多个互相不重复的数字”的开关，那么在同一次生成的随机数中，不同的位置可能会出现重复的数字；若是打开开关，那么在同一次生成中，不同的位置也不会产生同样的数字。",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onNavigateBack) {
            Text(text = "返回")
        }
    }
}
