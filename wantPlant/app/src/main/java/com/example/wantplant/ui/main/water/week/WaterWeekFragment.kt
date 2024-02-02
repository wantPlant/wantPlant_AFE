package com.example.wantplant.ui.main.water.week

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.wantplant.R
import com.example.wantplant.data.local.MonthDate
import com.example.wantplant.data.local.WeekDate
import com.example.wantplant.data.remote.tag.response.TagMonthGetResult
import com.example.wantplant.databinding.FragmentWaterWeekBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.month.WaterMonthDayRVAdapter
import com.example.wantplant.ui.main.water.month.WaterMonthFragment
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class WaterWeekFragment : Fragment(), WaterWeekGoalDialogInterface {
    private lateinit var binding : FragmentWaterWeekBinding
    private lateinit var standardDate: LocalDate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterWeekBinding.inflate(layoutInflater)

        standardDate = LocalDate.now()

        weekCalendar()

        val dayList = dayInMonthArray()
        Log.d("dayList", dayList.toString())
        val dayListManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val dayListAdapter = WaterWeekDayRVAdapter(dayList)
        binding.waterWeekDayListRv.apply {
            layoutManager = dayListManager
            adapter = dayListAdapter
        }

        onClickListener()

        initGoalRecyclerView()

        initPotRecyclerView()

        initGardenRecyclerView()

        return binding.root
    }

    private fun weekCalendar() {
        binding.waterWeekYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"

        binding.waterWeekBackIv.setOnClickListener {
            standardDate = standardDate.minusMonths(1)
            binding.waterWeekYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
//            getMonthTagAPI(standardDate)
            val dayList = dayInMonthArray()
            Log.d("dayList", dayList.toString())
            val dayListManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            val dayListAdapter = WaterWeekDayRVAdapter(dayList)
            binding.waterWeekDayListRv.apply {
                layoutManager = dayListManager
                adapter = dayListAdapter
            }
        }

        binding.waterWeekForwardIv.setOnClickListener {
            standardDate = standardDate.plusMonths(1)
            binding.waterWeekYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
//            getMonthTagAPI(standardDate)
            val dayList = dayInMonthArray()
            Log.d("dayList", dayList.toString())
            val dayListManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            val dayListAdapter = WaterWeekDayRVAdapter(dayList)
            binding.waterWeekDayListRv.apply {
                layoutManager = dayListManager
                adapter = dayListAdapter
            }
        }
    }

    private fun dayInMonthArray(): MutableList<WeekDate> {
        var yearMonth = YearMonth.from(standardDate)
        val dayList = ArrayList<WeekDate>()

        // 해당 월의 마지막 날짜 가져오기(결과: 1월이면 31)
        var lastDay = yearMonth.lengthOfMonth()
        // 해당 월의 첫번째 날 가져오기(결과: 2023-01-01)
        var firstDay = standardDate.withDayOfMonth(1)
        // 첫 번째날 요일 가져오기(결과: 월 ~일이 1~7에 대응되어 나타남)
        var dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1..42) {
            if (i > lastDay) {
                break
            }
            else {
                dayList.add(
                    WeekDate(LocalDate.of(standardDate.year, standardDate.monthValue, i))
                )
            }
        }
        return dayList
    }

    // YYYY 형식으로 포맷
    private fun yearFromDate(date: LocalDate?): String? {
        var formatter = DateTimeFormatter.ofPattern("yyyy")
        return date?.format(formatter)
    }

    // MM 형식으로 포맷
    private fun monthFromDate(date: LocalDate?): String? {
        var formatter = DateTimeFormatter.ofPattern("MM")
        return date?.format(formatter)
    }

    private fun onClickListener() {
        binding.waterWeekChangeCalendarLl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, WaterMonthFragment()).addToBackStack(tag)
                .commitAllowingStateLoss()
        }
    }

    private fun initGoalRecyclerView() {
        binding.waterWeekGoalRv.apply {
            adapter = WaterWeekGoalRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initPotRecyclerView() {
        val weekPotManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.waterWeekPotTitleRv.apply {
            adapter = WaterWeekPotTitleRVAdapter()
            layoutManager = weekPotManager
        }
    }

    private fun initGardenRecyclerView() {
        val weekGardenManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.waterWeekGardenTitleRv.apply {
            adapter = WaterWeekGardenTitleRVAdapter()
            layoutManager = weekGardenManager
        }
    }

    private fun showDialog(formattedDate: String) {
        binding.waterWeekAddGoalLl.setOnClickListener {
            val waterWeekGoalDialog = WaterWeekGoalDialog(binding.root.context, this, formattedDate)
            waterWeekGoalDialog.show()
        }
    }

    override fun onCancelClicked() {

    }

    override fun onCompleteClicked() {

    }

}