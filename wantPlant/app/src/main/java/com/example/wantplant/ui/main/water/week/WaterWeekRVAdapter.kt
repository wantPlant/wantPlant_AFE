package com.example.wantplant.ui.main.water.week

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.WeekDate
import com.example.wantplant.databinding.ItemWaterWeekBinding
import com.example.wantplant.ui.main.water.month.WaterMonthDayRVAdapter
import java.util.Calendar
import java.util.Date

class WaterWeekRVAdapter(private val dateClickedListener: onDateClickedListener): RecyclerView.Adapter<WaterWeekRVAdapter.ViewHolder>(), WaterWeekDayRVAdapter.onDateSelectedListener {
    val center = Int.MAX_VALUE / 2
    private var calendar = Calendar.getInstance()

    interface onDateClickedListener {
        fun onDateClicked(formattedDate: String)
    }

    inner class ViewHolder(val binding: ItemWaterWeekBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekRVAdapter.ViewHolder {
        val binding: ItemWaterWeekBinding = ItemWaterWeekBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekRVAdapter.ViewHolder, position: Int) {
        // Calendar의 time을 현재 날짜로 초기화
        calendar.time = Date()

        // set을 사용하여 현재 월의 1일로 이동
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // add를 사용하여 월 단위로 'position-center' 만큼 이동
        // center = Int.MAX_VALUE/2이므로 리스트를 자로 스크롤 할 경우 position - center는 -1, 우로 스크롤 할 경우 +1
        // 이렇게 구한 값을 월 단위로 이동함으로써 이전, 이후 월을 구할 수가 있음
        calendar.add(Calendar.MONTH, position - center)
        holder.binding.itemWeekText.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"

        // 현재의 월을 저장
        val tempMonth = calendar.get(Calendar.MONTH)

        // 그 월에서 보여줄 일들을 구함
        // Grid 타입의 RecyclerView를 사용하여 각 날짜를 보여줌
        // 6주 * 7일의 날짜를 표시하며 각 정보는 dayList에 저장하여 AdapterDay의 파라미터로 줌
//        var dayList: MutableList<WeekDate> = MutableList(6 * 7) { Date() }
        var dayList : MutableList<WeekDate> = MutableList(6*7){WeekDate(Date(), false)}
        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k].date = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }

        val dayListManager = LinearLayoutManager(holder.binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        val dayListAdapter = WaterWeekDayRVAdapter(tempMonth, dayList, this)

        holder.binding.itemWeekDayListRv.apply {
            layoutManager = dayListManager
            adapter = dayListAdapter
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onDateSelected(formattedDate: String) {
        Log.d("날짜가 잘 왔나용?", "Selected Date: $formattedDate")
        dateClickedListener.onDateClicked(formattedDate)
    }

}