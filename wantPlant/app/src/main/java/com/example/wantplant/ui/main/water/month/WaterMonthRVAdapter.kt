package com.example.wantplant.ui.main.water.month

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterMonthBinding
import java.util.Calendar
import java.util.Date

class WaterMonthRVAdapter: RecyclerView.Adapter<WaterMonthRVAdapter.ViewHolder>() {
    val center = Int.MAX_VALUE / 2
    private var calendar = Calendar.getInstance()

    inner class ViewHolder(val binding: ItemWaterMonthBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWaterMonthBinding = ItemWaterMonthBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 보여주고자 하는 월 구현

        // Calendar의 time을 현재 날짜로 초기화
        calendar.time = Date()

        // set을 사용하여 현재 월의 1일로 이동
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // add를 사용하여 월 단위로 'position-center' 만큼 이동
        // center = Int.MAX_VALUE/2이므로 리스트를 자로 스크롤 할 경우 position - center는 -1, 우로 스크롤 할 경우 +1
        // 이렇게 구한 값을 월 단위로 이동함으로써 이전, 이후 월을 구할 수가 있음
        calendar.add(Calendar.MONTH, position - center)
        holder.binding.itemMonthText.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"

        // 현재의 월을 저장
        val tempMonth = calendar.get(Calendar.MONTH)

        // 그 월에서 보여줄 일들을 구함
        // Grid 타입의 RecyclerView를 사용하여 각 날짜를 보여줌
        // 6주 * 7일의 날짜를 표시하며 각 정보는 dayList에 저장하여 AdapterDay의 파라미터로 줌
        var dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }

        val dayListManager = GridLayoutManager(holder.binding.root.context, 7)
        val dayListAdapter = WaterMonthDayRVAdapter(tempMonth, dayList)

        holder.binding.itemMonthDayList.apply {
            layoutManager = dayListManager
            adapter = dayListAdapter
        }
    }

    override fun getItemCount(): Int {

        // Int.MAX_VALUE: 리스트의 항목 개수가 큰 수로 설정되어 있음
        // 리스트를 좌우로 스크롤하였을 경우 이전 월과 이후 월들을 보여주기 위함
        // WaterFragment에서 Int.MAX_VALUE에서 항목이 시작되도록 설정
        // 시작 위치인 Int.MAX_VALUE/2를 현재 월로 설정하여 이동할 수 있게 하면 좌우로 실제로는 끝은 있지만, 거의 수억번 스크롤 가능
        return Int.MAX_VALUE
    }
}