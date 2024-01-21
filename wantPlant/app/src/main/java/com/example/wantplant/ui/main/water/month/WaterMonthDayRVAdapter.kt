package com.example.wantplant.ui.main.water.month

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterMonthDayBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterMonthDayRVAdapter(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<WaterMonthDayRVAdapter.ViewHolder>() {
    val ROW = 6

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    inner class ViewHolder(val binding: ItemWaterMonthDayBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWaterMonthDayBinding = ItemWaterMonthDayBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemDayLayout.setOnClickListener {
            val formattedDate = dateFormat.format(dayList[position])
            Log.d("날짜", formattedDate)
        }
        holder.binding.itemDayText.text = dayList[position].date.toString()

        holder.binding.itemDayText.setTextColor(when(position % 7) {
            // 일요일 색상
            0 -> Color.RED

            // 토요일 색상
            6 -> Color.BLUE
            else -> Color.BLACK
        })

        // tempMonth로 현재 월이 아닌 날짜의 경우 alpha를 낮추어 투명도를 주어 현재 월의 날짜와 다르게 표시
        if(tempMonth != dayList[position].month) {
            holder.binding.itemDayText.alpha = 0.4f
        }
    }

    // (ROW == 6주) * 7일로 총 42개의 날짜 표시
    override fun getItemCount(): Int {
        return ROW * 7
    }
}