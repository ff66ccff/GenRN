package com.randomnumbergenerator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private lateinit var etMin: TextInputEditText
    private lateinit var etMax: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var btnGenerate: MaterialButton
    private lateinit var tvResult: MaterialTextView
    private lateinit var btnRecord: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        loadConfig()
        setupListeners()
    }

    private fun initViews() {
        etMin = findViewById(R.id.et_min)
        etMax = findViewById(R.id.et_max)
        etCount = findViewById(R.id.et_count)
        btnGenerate = findViewById(R.id.btn_generate)
        tvResult = findViewById(R.id.tv_result)
        btnRecord = findViewById(R.id.btn_record)
    }

    private fun loadConfig() {
        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        etMin.setTextIfValid(prefs.getInt("min", 0))
        etMax.setTextIfValid(prefs.getInt("max", 0))
        etCount.setTextIfValid(prefs.getInt("count", 0))
        RandomNumberManager.loadState(this)
    }

    private fun TextInputEditText.setTextIfValid(value: Int) {
        setText(if (value != 0) value.toString() else "")
    }

    private fun setupListeners() {
        btnGenerate.setOnClickListener {
            val min = etMin.text.toString().toIntOrNull()
            val max = etMax.text.toString().toIntOrNull()
            val count = etCount.text.toString().toIntOrNull()

            when {
                min == null || max == null || count == null ->
                    showToast("请填写所有有效数字")
                min > max ->
                    showToast("最小值不能大于最大值")
                RandomNumberManager.preselectedNumbers.size > count ->
                    showToast("预选数量超过生成数量")
                else -> generateNumbers(min, max, count)
            }
        }

        btnRecord.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
        }

        setupTextWatchers()
    }

    private fun generateNumbers(min: Int, max: Int, count: Int) {
        if (RandomNumberManager.shouldResetPool(min, max)) {
            RandomNumberManager.initRange(min, max)
        }

        try {
            val numbers = RandomNumberManager.generateNext(count)
            tvResult.text = buildString {
                append("已生成 ")
                append(numbers.size)
                append(" 个数字：\n")
                append(numbers.joinToString(", "))
            }
            RandomNumberManager.saveState(this)
        } catch (e: Exception) {
            showToast("生成失败：${e.message}")
        }
    }

    private fun setupTextWatchers() {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = savePreferences()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        etMin.addTextChangedListener(watcher)
        etMax.addTextChangedListener(watcher)
        etCount.addTextChangedListener(watcher)
    }

    private fun savePreferences() {
        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().apply {
            putInt("min", etMin.text.toString().toIntOrNull() ?: 0)
            putInt("max", etMax.text.toString().toIntOrNull() ?: 0)
            putInt("count", etCount.text.toString().toIntOrNull() ?: 0)
        }.apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}