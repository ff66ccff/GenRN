package com.randomnumbergenerator

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class RecordActivity : AppCompatActivity() {

    private lateinit var rvGenerated: RecyclerView
    private lateinit var rvAvailable: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        rvGenerated = findViewById(R.id.rv_generated)
        rvAvailable = findViewById(R.id.rv_available)

        rvGenerated.layoutManager = LinearLayoutManager(this)
        rvAvailable.layoutManager = LinearLayoutManager(this)

        // 注意：generatedNumbers现在是一个List<List<Int>>，每个子List代表一次生成的记录
        rvGenerated.adapter = GenerationAdapter(RandomNumberManager.generatedNumbers)
        rvAvailable.adapter = AvailableAdapter(RandomNumberManager.pool)

        findViewById<MaterialToolbar>(R.id.topAppBar).setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * 修改预选数字的提示：
     * 原来直接显示preselectedNumbers.size，现改为剩余可预选数量 = generationSize - 已预选数量
     */
    private fun showSelectionFeedback(number: Int, isSelected: Boolean) {
        val remaining = RandomNumberManager.generationSize - RandomNumberManager.preselectedNumbers.size
        val message = if (isSelected) {
            "已预选数字: $number (剩余可预选: $remaining)"
        } else {
            "已取消预选: $number (剩余可预选: $remaining)"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 生成数字记录适配器
     * 显示格式为：第x次生成的数字为：number1,number2,...,numberx
     */
    inner class GenerationAdapter(private val rounds: List<List<Int>>) :
        RecyclerView.Adapter<GenerationAdapter.GenerationViewHolder>() {

        inner class GenerationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvRecord: TextView = itemView.findViewById(R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenerationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_list_item_custom, parent, false)
            return GenerationViewHolder(view)
        }

        override fun onBindViewHolder(holder: GenerationViewHolder, position: Int) {
            val round = rounds[position]
            holder.tvRecord.text = "第${position + 1}次生成的数字为：${round.joinToString(",")}"
        }

        override fun getItemCount(): Int = rounds.size
    }

    /**
     * 可选数字适配器
     *
     * 注意：预选的数字不会从列表中移除，只是改变背景颜色，
     * 这样满足“预选的数字不应该单独列出来”的要求，且生成数字总数依然固定。
     */
    inner class AvailableAdapter(private val numbers: List<Int>) :
        RecyclerView.Adapter<AvailableAdapter.AvailableViewHolder>() {

        inner class AvailableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvNumber: TextView = itemView.findViewById(R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_list_item_custom, parent, false)
            return AvailableViewHolder(view)
        }

        override fun onBindViewHolder(holder: AvailableViewHolder, position: Int) {
            val number = numbers[position]
            holder.tvNumber.text = number.toString()

            // 如果该数字已被预选，则改变背景色
            if (RandomNumberManager.preselectedNumbers.contains(number)) {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@RecordActivity,
                        R.color.selected_background
                    )
                )
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            holder.itemView.setOnClickListener {
                val isSelected = RandomNumberManager.togglePreselection(number)
                notifyItemChanged(position)
                showSelectionFeedback(number, isSelected)
                RandomNumberManager.saveState(this@RecordActivity) // 保存状态
            }
        }

        override fun getItemCount(): Int = numbers.size
    }
}
