package com.randomnumbergenerator

import android.content.Context

object RandomNumberManager {

    var min: Int = 0
    var max: Int = 0
    var pool = mutableListOf<Int>()
    // 修改generatedNumbers为存储每次生成的一组数字（即生成“轮次”）
    var generatedNumbers = mutableListOf<List<Int>>()
    val preselectedNumbers = mutableSetOf<Int>()

    // 新增generationSize属性，用于记录每次生成数字的数量（例如一次生成5个数字）
    var generationSize: Int = 0

    fun initRange(min: Int, max: Int) {
        this.min = min
        this.max = max
        resetPool()
    }

    fun resetPool() {
        pool = (min..max).toMutableList()
        generatedNumbers.clear()
        preselectedNumbers.clear()
    }

    /**
     * 生成下一轮数字
     * 1. 若存在预选数字，则直接使用预选数字，并从pool中移除
     * 2. 自动生成剩余数字，总数固定为count（例如5个）
     * 3. 将本轮生成的数字以列表形式存入generatedNumbers中
     */
    fun generateNext(count: Int): List<Int> {
        generationSize = count
        val validPreselected = preselectedNumbers.filter { it in min..max }
        val remainingCount = count - validPreselected.size
        if (remainingCount < 0) {
            throw IllegalArgumentException("预选数字数量超过生成数量")
        }

        val results = mutableListOf<Int>()
        results.addAll(validPreselected)
        pool.removeAll(validPreselected)

        var need = remainingCount
        while (need > 0) {
            if (pool.isEmpty()) resetPool()
            val index = pool.indices.random()
            val number = pool.removeAt(index)
            results.add(number)
            need--
        }

        preselectedNumbers.clear()
        generatedNumbers.add(results)
        return results
    }

    /**
     * 手动选择数字
     * 注意：这里将手动选择的数字单独作为一次生成记录存储
     */
    fun manualSelect(number: Int): Boolean {
        return if (pool.contains(number)) {
            pool.remove(number)
            generatedNumbers.add(listOf(number))
            true
        } else {
            false
        }
    }

    /**
     * 切换数字的预选状态
     */
    fun togglePreselection(number: Int): Boolean {
        return if (preselectedNumbers.contains(number)) {
            preselectedNumbers.remove(number)
            false
        } else {
            preselectedNumbers.add(number)
            true
        }
    }

    /**
     * 保存状态
     *
     * 注意：由于generatedNumbers现在为List<List<Int>>，因此采用分号分隔各轮记录，
     * 每一轮内部用逗号分隔数字
     */
    fun saveState(context: Context) {
        val prefs = context.getSharedPreferences("RandomNumberPrefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("min", min)
            putInt("max", max)
            putString("pool", pool.joinToString(","))
            putString("generatedNumbers", generatedNumbers.joinToString(";") { it.joinToString(",") })
            apply()
        }
    }

    /**
     * 加载状态
     */
    fun loadState(context: Context) {
        val prefs = context.getSharedPreferences("RandomNumberPrefs", Context.MODE_PRIVATE)
        min = prefs.getInt("min", 0)
        max = prefs.getInt("max", 0)
        pool = prefs.getString("pool", "")
            ?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map { it.toInt() }
            ?.toMutableList() ?: mutableListOf()

        val generatedStr = prefs.getString("generatedNumbers", "") ?: ""
        generatedNumbers = if (generatedStr.isEmpty()) {
            mutableListOf()
        } else {
            generatedStr.split(";").map { roundStr ->
                roundStr.split(",").filter { it.isNotEmpty() }.map { it.toInt() }
            }.toMutableList()
        }
    }

    fun shouldResetPool(newMin: Int, newMax: Int): Boolean {
        return pool.isEmpty() || min != newMin || max != newMax
    }
}
