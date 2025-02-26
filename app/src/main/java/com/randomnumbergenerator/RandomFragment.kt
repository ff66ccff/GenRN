package com.randomnumbergenerator.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.random.Random

@Composable
fun RandomScreen(onNavigateToAbout: () -> Unit) {
    var minValue by remember { mutableStateOf("") }
    var maxValue by remember { mutableStateOf("") }
    var numToGenerate by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("生成的随机数将在此显示") }
    var history by remember { mutableStateOf(listOf<String>()) }
    var showDialog by remember { mutableStateOf(false) }
    var isUniqueEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // 使用 rememberSaveable 保存是否已清空过历史的状态
    var isHistoryCleared by rememberSaveable { mutableStateOf(false) }

    // 仅在首次启动应用时清空历史记录
    LaunchedEffect(Unit) {
        if (!isHistoryCleared) {
            history = emptyList()
            saveHistory(context, history)
            isHistoryCleared = true // 标记为已清空
        } else {
            history = loadHistory(context)
        }

        // 加载保存的其他值
        loadSavedValues(context, onLoad = { min, max, num, isUnique ->
            minValue = min
            maxValue = max
            numToGenerate = num
            isUniqueEnabled = isUnique
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
                        val randomNumbers = generateUniqueRandomNumbers(context, count, min, max)
                        resultText = "生成的随机数为: ${randomNumbers.joinToString(", ")}"

                        val historyRecord = "第${history.size + 1}次生成: ${randomNumbers.joinToString(", ")}"
                        history = history + historyRecord

                        // 保存历史记录和其他数据
                        saveHistory(context, history)
                        saveValues(context, minValue, maxValue, numToGenerate, isUniqueEnabled)
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

// 全局历史文件
private const val JSON_FILE_NAME = "generated_numbers.json"

// 从JSON文件中加载已生成的数字
fun loadGeneratedNumbers(context: Context): MutableSet<Int> {
    val file = File(context.filesDir, JSON_FILE_NAME)
    return if (file.exists()) {
        val jsonData = file.readText()
        Json.decodeFromString(jsonData)
    } else {
        mutableSetOf()
    }
}

// 保存生成的数字到JSON文件
fun saveGeneratedNumbers(context: Context, generatedNumbers: MutableSet<Int>) {
    val file = File(context.filesDir, JSON_FILE_NAME)
    val jsonData = Json.encodeToString(generatedNumbers)
    file.writeText(jsonData)
}

// 清空生成历史文件
fun clearGeneratedNumbers(context: Context) {
    val file = File(context.filesDir, JSON_FILE_NAME)
    file.writeText(Json.encodeToString(mutableSetOf<Int>()))
}

// 生成不重复的随机数
fun generateUniqueRandomNumbers(context: Context, count: Int, min: Int, max: Int): List<Int> {
    val generatedNumbers = loadGeneratedNumbers(context)
    val result = mutableListOf<Int>()
    val rangeSize = max - min + 1

    if (count > rangeSize - generatedNumbers.size) {
        throw IllegalArgumentException("请求的数量超过可生成的最大唯一数字数量")
    }

    val random = Random(System.currentTimeMillis())
    while (result.size < count) {
        val number = random.nextInt(min, max + 1)
        if (number !in generatedNumbers) {
            result.add(number)
            generatedNumbers.add(number)
        }
    }

    saveGeneratedNumbers(context, generatedNumbers)

    if (generatedNumbers.size >= rangeSize) {
        clearGeneratedNumbers(context)
    }

    return result
}

// SharedPreferences 保存历史记录
fun saveHistory(context: Context, history: List<String>) {
    val sharedPreferences = context.getSharedPreferences("RandomPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val historyJson = Json.encodeToString(history)
    editor.putString("history", historyJson)
    editor.apply()
}

// 从 SharedPreferences 加载历史记录
fun loadHistory(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences("RandomPrefs", Context.MODE_PRIVATE)
    val historyJson = sharedPreferences.getString("history", "[]")
    return if (historyJson != null) Json.decodeFromString(historyJson) else emptyList()
}

// 保存输入值
fun saveValues(context: Context, minValue: String, maxValue: String, numToGenerate: String, isUniqueEnabled: Boolean) {
    val sharedPreferences = context.getSharedPreferences("RandomPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("minValue", minValue)
    editor.putString("maxValue", maxValue)
    editor.putString("numToGenerate", numToGenerate)
    editor.putBoolean("isUniqueEnabled", isUniqueEnabled)
    editor.apply()
}

// 加载输入值
fun loadSavedValues(context: Context, onLoad: (min: String, max: String, num: String, isUnique: Boolean) -> Unit) {
    val sharedPreferences = context.getSharedPreferences("RandomPrefs", Context.MODE_PRIVATE)
    val minValue = sharedPreferences.getString("minValue", "") ?: ""
    val maxValue = sharedPreferences.getString("maxValue", "") ?: ""
    val numToGenerate = sharedPreferences.getString("numToGenerate", "") ?: ""
    val isUniqueEnabled = sharedPreferences.getBoolean("isUniqueEnabled", false)
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
