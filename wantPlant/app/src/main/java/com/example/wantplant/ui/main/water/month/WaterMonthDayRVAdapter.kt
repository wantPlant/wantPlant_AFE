package com.example.wantplant.ui.main.water.month

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterMonthDayBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterMonthDayRVAdapter(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<WaterMonthDayRVAdapter.ViewHolder>(), WaterMonthDialogInterface {
    private val row = 6

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    inner class ViewHolder(val binding: ItemWaterMonthDayBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWaterMonthDayBinding = ItemWaterMonthDayBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.binding.root.context

        holder.binding.itemDayLayout.setOnClickListener {
            val formattedDate = dateFormat.format(dayList[position])
            Log.d("날짜", formattedDate)
            holder.binding.waterMonthDaySelectIv.visibility = View.VISIBLE
            val waterMonthDialog = WaterMonthDialog(context, this, formattedDate)
            waterMonthDialog.show()
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
            holder.binding.itemDayLayout.alpha = 0.4f
            holder.binding.itemDayLayout.isClickable = false
        }

        val tagListManager = LinearLayoutManager(context)
        val tagListAdapter = WaterMonthDayTagRVAdapter()
        holder.binding.waterMonthDayRv.apply {
            layoutManager = tagListManager
            adapter = tagListAdapter
        }

        val isToday = isToday(dayList[position])

        holder.binding.waterMonthDayTodayIv.visibility = if (isToday) View.VISIBLE else View.INVISIBLE
    }

    // (ROW == 6주) * 7일로 총 42개의 날짜 표시
    override fun getItemCount(): Int {
        return row * 7
    }

    private fun isToday(date: Date): Boolean {
        val today = Date()
        val formattedToday = dateFormat.format(today)
        val formattedDate = dateFormat.format(date)
        return formattedToday == formattedDate
    }

    override fun onCancelClicked() {

    }

    override fun onCompleteClicked() {

    }

}