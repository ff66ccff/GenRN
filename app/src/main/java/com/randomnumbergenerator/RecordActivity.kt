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

        rvGenerated.adapter = NumberAdapter(RandomNumberManager.generatedNumbers)
        rvAvailable.adapter = AvailableAdapter(RandomNumberManager.pool)

        findViewById<MaterialToolbar>(R.id.topAppBar).setNavigationOnClickListener {
            finish()
        }
    }

    private fun showSelectionFeedback(number: Int, isSelected: Boolean) {
        val message = if (isSelected) {
            "已预选数字: $number (剩余可预选: ${RandomNumberManager.preselectedNumbers.size})"
        } else {
            "已取消预选: $number"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inner class NumberAdapter(private val numbers: List<Int>) :
        RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

        inner class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvNumber: TextView = itemView.findViewById(R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_list_item_custom, parent, false)
            return NumberViewHolder(view)
        }

        override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
            holder.tvNumber.text = numbers[position].toString()
        }

        override fun getItemCount(): Int = numbers.size
    }

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
                RandomNumberManager.saveState(this@RecordActivity) // 新增保存
            }
        }

        override fun getItemCount(): Int = numbers.size
    }
}