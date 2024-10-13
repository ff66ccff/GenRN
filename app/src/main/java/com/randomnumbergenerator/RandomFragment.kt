package com.randomnumbergenerator.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import kotlin.random.Random

@Composable
fun RandomScreen(onNavigateToAbout: () -> Unit) {
    var minValue by remember { mutableStateOf("") }
    var maxValue by remember { mutableStateOf("") }
    var numToGenerate by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("生成的随机数将在此显示") }
    var history by remember { mutableStateOf(listOf<String>()) }
    var showDialog by remember { mutableStateOf(false) }
    var isUniqueEnabled by remember { mutableStateOf(false) }  // 新增互不重复开关
    val context = LocalContext.current

    // 记录每个位置的历史生成记录，确保不重复
    val positionHistory = remember { mutableStateListOf<MutableList<Int>>() }

    // 加载保存的值，包括开关状态
    LaunchedEffect(Unit) {
        loadSavedValues(context, onLoad = { min, max, num, isUnique ->
            minValue = min
            maxValue = max
            numToGenerate = num
            isUniqueEnabled = isUnique  // 恢复开关状态
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "随机数生成器",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = minValue,
                onValueChange = { minValue = it },
                label = { Text("最小值") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                value = maxValue,
                onValueChange = { maxValue = it },
                label = { Text("最大值") },
                modifier = Modifier.weight(1f)
            )
        }

        TextField(
            value = numToGenerate,
            onValueChange = { numToGenerate = it },
            label = { Text("生成数量") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "一次性生成多个互相不重复的数字")
            Switch(
                checked = isUniqueEnabled,
                onCheckedChange = { isUniqueEnabled = it }
            )
        }

        Button(
            onClick = {
                val min = minValue.toIntOrNull()
                val max = maxValue.toIntOrNull()
                val count = numToGenerate.toIntOrNull()

                if (min != null && max != null && count != null && min < max && count > 0) {
                    if (count <= (max - min + 1)) {
                        if (positionHistory.size != count) {
                            positionHistory.clear()
                            repeat(count) { positionHistory.add(mutableListOf()) }
                        }

                        // 根据开关状态决定生成逻辑
                        val randomNumbers = if (isUniqueEnabled) {
                            generateGloballyUniqueRandomNumbers(count, min, max, positionHistory)
                        } else {
                            generatePositionUniqueRandomNumbers(count, min, max, positionHistory)
                        }

                        resultText = "生成的随机数为: ${randomNumbers.joinToString(", ")}"

                        val historyRecord = "第${history.size + 1}次生成: ${randomNumbers.joinToString(", ")}"
                        history = history + historyRecord

                        saveValues(context, minValue, maxValue, numToGenerate, isUniqueEnabled)  // 保存开关状态
                    } else {
                        resultText = "请求的数字数量超过可能的最大唯一数字数量"
                    }
                } else {
                    Toast.makeText(context, "输入有误", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("生成")
        }

        Text(
            text = resultText,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        )

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("查看历史记录")
        }

        Button(
            onClick = onNavigateToAbout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("关于")
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "历史记录", style = MaterialTheme.typography.titleLarge)
                        Box(modifier = Modifier.weight(1f)) {
                            LazyColumn {
                                items(history) { record ->
                                    Text(record)
                                }
                            }
                        }
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("关闭")
                        }
                    }
                }
            }
        }
    }
}

// 生成每个位置互不重复的随机数（局部去重逻辑）
fun generatePositionUniqueRandomNumbers(count: Int, min: Int, max: Int, history: List<MutableList<Int>>): List<Int> {
    val result = mutableListOf<Int>()

    for (i in 0 until count) {
        if (history[i].size == (max - min + 1)) {
            history[i].clear()
        }

        val availableNumbers = (min..max).filter { it !in history[i] }

        val number = availableNumbers.random()
        result.add(number)

        history[i].add(number)
    }

    return result
}

// 生成全局互不重复的随机数（全局去重逻辑）
fun generateGloballyUniqueRandomNumbers(count: Int, min: Int, max: Int, history: List<MutableList<Int>>): List<Int> {
    val result = mutableListOf<Int>()
    val globalHistory = mutableSetOf<Int>() // 全局历史记录

    for (i in 0 until count) {
        if (history[i].size == (max - min + 1)) {
            history[i].clear()
        }

        val availableNumbers = (min..max).filter { it !in history[i] && it !in globalHistory }

        val number = availableNumbers.random()
        result.add(number)

        history[i].add(number)
        globalHistory.add(number) // 更新全局历史记录
    }

    return result
}

// 修改后的保存函数，增加保存开关状态
fun saveValues(context: Context, minValue: String, maxValue: String, numToGenerate: String, isUniqueEnabled: Boolean) {
    val sharedPreferences = context.getSharedPreferences("RandomPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("minValue", minValue)
    editor.putString("maxValue", maxValue)
    editor.putString("numToGenerate", numToGenerate)
    editor.putBoolean("isUniqueEnabled", isUniqueEnabled)  // 保存开关状态
    editor.apply()
}

// 修改后的加载函数，加载开关状态
fun loadSavedValues(context: Context, onLoad: (min: String, max: String, num: String, isUnique: Boolean) -> Unit) {
    val sharedPreferences = context.getSharedPreferences("RandomPrefs", Context.MODE_PRIVATE)
    val minValue = sharedPreferences.getString("minValue", "") ?: ""
    val maxValue = sharedPreferences.getString("maxValue", "") ?: ""
    val numToGenerate = sharedPreferences.getString("numToGenerate", "") ?: ""
    val isUniqueEnabled = sharedPreferences.getBoolean("isUniqueEnabled", false)  // 加载开关状态
    onLoad(minValue, maxValue, numToGenerate, isUniqueEnabled)
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "关于",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "这是一个基于kotlin的随机数生成器，使用material design。\n本项目的官方网址为：https://github.com/ff66ccff/GenRN",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
