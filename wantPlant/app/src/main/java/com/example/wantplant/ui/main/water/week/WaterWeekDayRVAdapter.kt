package com.example.wantplant.ui.main.water.week

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterWeekDayBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterWeekDayRVAdapter(val tempMonth: Int, val dayList: MutableList<Date>): RecyclerView.Adapter<WaterWeekDayRVAdapter.ViewHolder>() {
    private val row = 6

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    inner class ViewHolder(val binding: ItemWaterWeekDayBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekDayRVAdapter.ViewHolder {
        val binding : ItemWaterWeekDayBinding = ItemWaterWeekDayBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekDayRVAdapter.ViewHolder, position: Int) {

        holder.binding.itemDayLayout.setOnClickListener {
            val formattedDate = dateFormat.format(dayList[position])
            Log.d("날짜", formattedDate)
            holder.binding.waterWeekDaySelectIv.visibility = View.VISIBLE
        }

        holder.binding.itemDateTv.text = dayList[position].date.toString()

        var dateDay = dayList[position].day.toString()

        if (dateDay == "0") {
            holder.binding.itemDayTv.text = "일"
        }
        else if (dateDay == "1") {
            holder.binding.itemDayTv.text = "월"
        }
        else if (dateDay == "2") {
            holder.binding.itemDayTv.text = "화"
        }
        else if (dateDay == "3") {
            holder.binding.itemDayTv.text = "수"
        }
        else if (dateDay == "4") {
            holder.binding.itemDayTv.text = "목"
        }
        else if (dateDay == "5") {
            holder.binding.itemDayTv.text = "금"
        }
        else {
            holder.binding.itemDayTv.text = "토"
        }

//        holder.binding.itemDayText.setTextColor(when(position % 7) {
//            // 일요일 색상
//            0 -> Color.RED
//
//            // 토요일 색상
//            6 -> Color.BLUE
//            else -> Color.BLACK
//        })

        // tempMonth로 현재 월이 아닌 날짜의 경우 안 보이게
        if(tempMonth != dayList[position].month) {
            holder.binding.itemDayLayout.isClickable = false
//            holder.binding.itemDayLayout.visibility = View.GONE
        }

        val isToday = isToday(dayList[position])

        holder.binding.waterWeekDayTodayIv.visibility = if (isToday) View.VISIBLE else View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return row * 7
    }

    private fun isToday(date: Date): Boolean {
        val today = Date()
        val formattedToday = dateFormat.format(today)
        val formattedDate = dateFormat.format(date)
        return formattedToday == formattedDate
    }
}