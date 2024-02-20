package com.example.wantplant.ui.main.water.week

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.WeekDate
import com.example.wantplant.databinding.ItemWaterWeekDayBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class WaterWeekDayRVAdapter(private val dayList: MutableList<WeekDate>): RecyclerView.Adapter<WaterWeekDayRVAdapter.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    interface DayClickListener {
        fun onWeekDayClick(formattedDate: String)
    }

    private lateinit var mDayClickListener: DayClickListener

    fun setWeekDayClick(dayClickListener: DayClickListener) {
        mDayClickListener = dayClickListener
    }

    inner class ViewHolder(val binding: ItemWaterWeekDayBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekDayRVAdapter.ViewHolder {
        val binding : ItemWaterWeekDayBinding = ItemWaterWeekDayBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekDayRVAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        // 오늘 날짜에 하트 표시
        if (dayList[position].date == LocalDate.now()) {
            holder.binding.waterWeekDayTodayIv.visibility = View.VISIBLE
        }
        else {
            holder.binding.waterWeekDayTodayIv.visibility = View.INVISIBLE
        }

        // 해당 날을 클릭 했을 때
        holder.binding.itemDayLayout.setOnClickListener {
            val formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            Log.d("날짜", dayList[position].date!!.format(formattedDate))

            notifyItemChanged(selectedPosition)

            // 날짜 전달
            mDayClickListener.onWeekDayClick(dayList[position].date!!.format(formattedDate))

            // 채운 하트 표시
            holder.binding.waterWeekDaySelectIv.visibility = View.VISIBLE

            selectedPosition = position
        }

        if (selectedPosition == position) {
            holder.binding.waterWeekDaySelectIv.visibility = View.VISIBLE
        } else {
            holder.binding.waterWeekDaySelectIv.visibility = View.INVISIBLE
        }

        if (dayList[position].date != null) {

            holder.binding.itemDateTv.text = dayList[position].date?.dayOfMonth.toString()

            if (dayList[position].date?.dayOfWeek.toString() == "SUNDAY") {
                holder.binding.itemDayTv.text = "일"
            }
            else if (dayList[position].date?.dayOfWeek.toString() == "MONDAY") {
                holder.binding.itemDayTv.text = "월"
            }
            else if (dayList[position].date?.dayOfWeek.toString() == "TUESDAY") {
                holder.binding.itemDayTv.text = "화"
            }
            else if (dayList[position].date?.dayOfWeek.toString() == "WEDNESDAY") {
                holder.binding.itemDayTv.text = "수"
            }
            else if (dayList[position].date?.dayOfWeek.toString() == "THURSDAY") {
                holder.binding.itemDayTv.text = "목"
            }
            else if (dayList[position].date?.dayOfWeek.toString() == "FRIDAY") {
                holder.binding.itemDayTv.text = "금"
            }
            else {
                holder.binding.itemDayTv.text = "토"
            }
        }

    }

    override fun getItemCount(): Int {
        return dayList.size
    }

}