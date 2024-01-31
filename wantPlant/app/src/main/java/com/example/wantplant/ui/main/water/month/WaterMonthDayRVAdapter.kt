package com.example.wantplant.ui.main.water.month

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.MonthDate
import com.example.wantplant.databinding.ItemWaterMonthDayBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WaterMonthDayRVAdapter(private val dayList: MutableList<MonthDate>): RecyclerView.Adapter<WaterMonthDayRVAdapter.ViewHolder>(){

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: ItemWaterMonthDayBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWaterMonthDayBinding = ItemWaterMonthDayBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val context = holder.binding.root.context

        // 해당 날을 클릭 했을 때
        holder.binding.itemDayLayout.setOnClickListener {
            val formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            Log.d("날짜", dayList[position].date!!.format(formattedDate))

            notifyItemChanged(selectedPosition)

            // 채운 하트 표시
            holder.binding.waterMonthDaySelectIv.visibility = View.VISIBLE

            selectedPosition = position

            // 태그 추가 dialog 띄우기
            val waterMonthDialog = WaterMonthDialog(context, dayList[position].date!!.format(formattedDate))
            waterMonthDialog.show()
        }

        if (selectedPosition == position) {
            holder.binding.waterMonthDaySelectIv.visibility = View.VISIBLE
        } else {
            holder.binding.waterMonthDaySelectIv.visibility = View.GONE
        }

        // 이번 달 날짜인 것 체크
        if (dayList[position].date != null) {
            holder.binding.itemDayText.text = dayList[position].date?.dayOfMonth.toString()
            val tagListManager = LinearLayoutManager(context)
            val tagListAdapter = WaterMonthDayTagRVAdapter(dayList[position].tag)
            holder.binding.waterMonthDayRv.apply {
                layoutManager = tagListManager
                adapter = tagListAdapter
            }
        }
        else {
            holder.binding.itemDayText.text = ""
        }

        // 오늘 날짜에 하트 표시
        if (dayList[position].date == LocalDate.now()) {
            holder.binding.waterMonthDayTodayIv.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return dayList.size
    }

}