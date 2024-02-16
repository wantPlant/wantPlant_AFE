package com.example.wantplant.ui.main.water.month

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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

class WaterMonthFragment : Fragment(), WaterMonthInterface {
    private lateinit var binding: FragmentWaterMonthBinding
    private lateinit var standardDate: LocalDate
    private lateinit var changeCalendarModeTextView: TextView

    @SuppressLint("ResourceAsColor")
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

        setTextViewColor()

        return binding.root
    }

    private fun setTextViewColor() {

        changeCalendarModeTextView = binding.waterMonthChangeCalendarTv

        val textData: String = changeCalendarModeTextView.text.toString()
        val builder = SpannableStringBuilder(textData)

        val color1 = ResourcesCompat.getColor(resources, R.color.wp_changeCalendar1, null)
        val setColor1 = ForegroundColorSpan(color1)
        builder.setSpan(setColor1, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        val color2 = ResourcesCompat.getColor(resources, R.color.wp_changeCalendar2, null)
        val setColor2 = ForegroundColorSpan(color2)
        builder.setSpan(setColor2, 1, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        changeCalendarModeTextView.text = builder

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

        binding.waterMonthYearTv.setOnClickListener {
            getMonthTagAPI(standardDate)
        }

    }

    // 월별 태그 api 연동
    private fun getMonthTagAPI(standardDate: LocalDate) {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val tagService = getRetrofit().create(TagRetrofitInterfaces::class.java)

        tagService.getMonthTag("Bearer $accessToken", year = Integer.parseInt(yearFromDate(standardDate)), month = Integer.parseInt(monthFromDate(standardDate))).enqueue(object: Callback<TagGetMonthResponse>
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
                            if (tagMap.containsKey(tagData.date)) {
                                // 이미 해당 날짜에 대한 리스트가 존재한다면, 리스트에 추가
                                tagMap[tagData.date]?.add(tagData)
                            } else {
                                // 해당 날짜에 대한 리스트가 존재하지 않는다면, 새로운 리스트 생성 후 추가
                                val newList = mutableListOf<TagMonthGetResult>()
                                newList.add(tagData)
                                tagMap[tagData.date] = newList
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


                    dayListAdapter.setDayClick(object: WaterMonthDayRVAdapter.ItemClickListener{
                        // 일 클릭 시 dialog
                        override fun onDayClick(formattedTagDate: String) {
                            val waterMonthDialog = WaterMonthDialog(requireContext(), formattedTagDate, this@WaterMonthFragment)
                            waterMonthDialog.show()
                        }

                        // 태그 클릭 시 dialog
                        override fun onTagClick2(tag: TagMonthGetResult) {
                            val waterMonthPatchDialog = WaterMonthPatchDialog(requireContext(), tag, this@WaterMonthFragment)
                            waterMonthPatchDialog.show()
                        }
                    })
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
            if (dayOfWeek == 7) { // 그 달의 첫날이 일요일 일 때 작동: 한칸 아래 줄부터 날짜 표시 되는 현상 막기 위해
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

    // dialog 확인 클릭 시
    override fun clickDialogComplete() {
        Log.d("TagAddInterface", "Success")
        monthCalendar()
    }

    // dialog 수정 클릭 시
    override fun clickDialogPatch() {
        Log.d("TagPatchInterface", "Success")
        monthCalendar()
    }

    // dialog 삭제 클릭 시
    override fun clickDialogDelete() {
        Log.d("TagDeleteInterface", "Success")
        monthCalendar()
    }

}