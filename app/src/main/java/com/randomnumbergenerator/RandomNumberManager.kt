package com.randomnumbergenerator

import android.content.Context

object RandomNumberManager {

    var min: Int = 0
    var max: Int = 0
    var pool = mutableListOf<Int>()
    var generatedNumbers = mutableListOf<Int>()
    val preselectedNumbers = mutableSetOf<Int>()

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

    fun generateNext(count: Int): List<Int> {
        val validPreselected = preselectedNumbers.filter { it in min..max }
        val remainingCount = count - validPreselected.size
        if (remainingCount < 0) {
            throw IllegalArgumentException("预选数字数量超过生成数量")
        }

        val results = validPreselected.toMutableList()
        pool.removeAll(validPreselected)
        generatedNumbers.addAll(validPreselected)

        var need = remainingCount
        while (need > 0) {
            if (pool.isEmpty()) resetPool()
            val number = pool.removeAt(pool.indices.random())
            results.add(number)
            generatedNumbers.add(number)
            need--
        }

        preselectedNumbers.clear()
        return results
    }

    fun manualSelect(number: Int): Boolean {
        return if (pool.contains(number)) {
            pool.remove(number)
            generatedNumbers.add(number)
            true
        } else {
            false
        }
    }

    fun togglePreselection(number: Int): Boolean {
        return if (preselectedNumbers.contains(number)) {
            preselectedNumbers.remove(number)
            false
        } else {
            preselectedNumbers.add(number)
            true
        }
    }

    fun saveState(context: Context) {
        val prefs = context.getSharedPreferences("RandomNumberPrefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("min", min)
            putInt("max", max)
            putString("pool", pool.joinToString(","))
            putString("generatedNumbers", generatedNumbers.joinToString(","))
            apply()
        }
    }

    fun loadState(context: Context) {
        val prefs = context.getSharedPreferences("RandomNumberPrefs", Context.MODE_PRIVATE)
        min = prefs.getInt("min", 0)
        max = prefs.getInt("max", 0)
        pool = prefs.getString("pool", "")
            ?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map { it.toInt() }
            ?.toMutableList() ?: mutableListOf()
        generatedNumbers = prefs.getString("generatedNumbers", "")
            ?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map { it.toInt() }
            ?.toMutableList() ?: mutableListOf()
    }

    fun shouldResetPool(newMin: Int, newMax: Int): Boolean {
        return pool.isEmpty() || min != newMin || max != newMax
    }
}