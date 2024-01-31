package com.example.wantplant.ui.main.water.month

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wantplant.R
import com.example.wantplant.data.local.MonthDate
import com.example.wantplant.data.remote.tag.TagRetrofitInterfaces
import com.example.wantplant.data.remote.tag.response.TagGetMonthResponse
import com.example.wantplant.data.remote.tag.response.TagMonthGetResult
import com.example.wantplant.databinding.FragmentWaterMonthBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.week.WaterWeekFragment
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class WaterMonthFragment : Fragment() {
    private lateinit var binding: FragmentWaterMonthBinding
    private lateinit var standardDate: LocalDate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterMonthBinding.inflate(layoutInflater)

        standardDate = LocalDate.now()

        binding.waterMonthChangeCalendarLl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, WaterWeekFragment()).addToBackStack(tag)
                .commitAllowingStateLoss()
        }

        monthCalendar()

        return binding.root
    }

    private fun monthCalendar() {

        binding.waterMonthYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
        getMonthTagAPI(standardDate)

        binding.waterMonthBackIv.setOnClickListener {
            standardDate = standardDate.minusMonths(1)
            binding.waterMonthYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
            getMonthTagAPI(standardDate)
        }

        binding.waterMonthForwardIv.setOnClickListener {
            standardDate = standardDate.plusMonths(1)
            binding.waterMonthYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
            getMonthTagAPI(standardDate)
        }

    }

    private fun getMonthTagAPI(standardDate: LocalDate) {

        val tagService = getRetrofit().create(TagRetrofitInterfaces::class.java)

        tagService.getMonthTag(year = Integer.parseInt(yearFromDate(standardDate)), month = Integer.parseInt(monthFromDate(standardDate))).enqueue(object: Callback<TagGetMonthResponse>
        {
            override fun onResponse(call: Call<TagGetMonthResponse>, response: Response<TagGetMonthResponse>)
            {
                Log.e("TagGet/ServerSuccess", response.message())
                Log.d("getMonth", response.body()?.result.toString())

                if (response.code() == 200 && response.isSuccessful) {
                    val tagList = response.body()?.result?.tagResponseDtos

                    var tagMap = HashMap<String, MutableList<TagMonthGetResult>>()
                    if (tagList != null) {
                        for(tagData: TagMonthGetResult in tagList){ // for each
                            if (tagMap.containsKey(tagData.date.toString())) {
                                // 이미 해당 날짜에 대한 리스트가 존재한다면, 리스트에 추가
                                tagMap[tagData.date.toString()]?.add(tagData)
                            } else {
                                // 해당 날짜에 대한 리스트가 존재하지 않는다면, 새로운 리스트 생성 후 추가
                                val newList = mutableListOf<TagMonthGetResult>()
                                newList.add(tagData)
                                tagMap[tagData.date.toString()] = newList
                            }
                        }
                    }

                    Log.d("tagMap", tagMap.toString())

                    val dayList = dayInMonthArray(tagMap)
                    Log.d("dayList", dayList.toString())
                    val dayListManager = GridLayoutManager(activity, 7)
                    val dayListAdapter = WaterMonthDayRVAdapter(dayList)
                    binding.waterMonthDayListRv.apply {
                        layoutManager = dayListManager
                        adapter = dayListAdapter
                    }
                }

            }

            override fun onFailure(call: Call<TagGetMonthResponse>, t: Throwable) {
                Log.e("TagGet/Failure", t.message.toString())
            }

        })
    }

    private fun dayInMonthArray(tagMap: HashMap<String, MutableList<TagMonthGetResult>>): MutableList<MonthDate> {
        var yearMonth = YearMonth.from(standardDate)
        val dayList = ArrayList<MonthDate>()

        // 해당 월의 마지막 날짜 가져오기(결과: 1월이면 31)
        var lastDay = yearMonth.lengthOfMonth()
        // 해당 월의 첫번째 날 가져오기(결과: 2023-01-01)
        var firstDay = standardDate.withDayOfMonth(1)
        // 첫 번째날 요일 가져오기(결과: 월 ~일이 1~7에 대응되어 나타남)
        var dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1..42) {
            if (dayOfWeek == 7) {//그 달의 첫날이 일요일일때 작동: 한칸 아래줄부터 날짜 표시되는 현상 막기위해
                if (i > lastDay) {
                    break
                }
                else {
                    dayList.add(
                        MonthDate(
                            LocalDate.of(
                                standardDate.year, standardDate.monthValue, i
                            ), tagMap[LocalDate.of(
                                standardDate.year, standardDate.monthValue, i
                            ).toString()]
                        )
                    )
                }
            }
            else if (i <= dayOfWeek) { // 끝에 빈칸 자르기 위해
                dayList.add(MonthDate(null, null))
            } else if (i > (lastDay + dayOfWeek)) {// 끝에 빈칸 자르기 위해
                break
            }

            else {
                dayList.add(
                    MonthDate(
                        LocalDate.of(
                            standardDate.year, standardDate.monthValue, i - dayOfWeek
                        ), tagMap[LocalDate.of(
                            standardDate.year, standardDate.monthValue, i - dayOfWeek
                        ).toString()]
                    )
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
}