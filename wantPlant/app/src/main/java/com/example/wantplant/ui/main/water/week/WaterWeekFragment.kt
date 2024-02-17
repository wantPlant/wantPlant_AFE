package com.example.wantplant.ui.main.water.week

import android.content.Context
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.R
import com.example.wantplant.data.local.WeekDate
import com.example.wantplant.data.remote.garden.GardenRetrofitInterfaces
import com.example.wantplant.data.remote.garden.response.GardenGetResponse
import com.example.wantplant.data.remote.garden.response.PotList
import com.example.wantplant.data.remote.goal.GoalRetrofitInterfaces
import com.example.wantplant.data.remote.goal.response.GoalTodoGetResponse
import com.example.wantplant.data.remote.todo.TodoRetrofitInterfaces
import com.example.wantplant.data.remote.todo.request.TodoPatchCompleteRequest
import com.example.wantplant.data.remote.todo.response.TodoPatchCompleteResponse
import com.example.wantplant.data.remote.todo.response.TodoPatchResponse
import com.example.wantplant.databinding.FragmentWaterWeekBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.month.WaterMonthDayRVAdapter
import com.example.wantplant.ui.main.water.month.WaterMonthFragment
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import retrofit2.Callback
import retrofit2.Response

class WaterWeekFragment : Fragment(), WaterWeekInterface {
    private lateinit var binding : FragmentWaterWeekBinding
    private lateinit var standardDate: LocalDate
    private lateinit var changeCalendarModeTextView: TextView

    private var clickdate: String? = ""
    private var clickPotId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterWeekBinding.inflate(layoutInflater)

        standardDate = LocalDate.now()

        weekCalendar()

//        val dayList = dayInMonthArray()
//        Log.d("dayList", dayList.toString())
//        val dayListManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val dayListAdapter = WaterWeekDayRVAdapter(dayList)
//        binding.waterWeekDayListRv.apply {
//            layoutManager = dayListManager
//            adapter = dayListAdapter
//        }

        onClickListener()

//        initGoalRecyclerView()

        binding.waterWeekAddGoalLl.setOnClickListener {
            val waterWeekGoalDialog = WaterWeekGoalDialog(requireContext(), this@WaterWeekFragment, clickdate.toString(), clickPotId)
            waterWeekGoalDialog.show()
        }

        setTextViewColor()

//        initPotRecyclerView()

//        initGardenRecyclerView()

        // 정원 리사이클러뷰 api 연동
        getGarden()

        if (clickdate != null && clickPotId != 0.toLong()) {
            // 날짜별 목표, 할 일 api 연동
            getGoalTodo()
        }

        if (clickdate != "" && clickPotId != 0.toLong()) {
            binding.waterWeekAddGoalLl.visibility = View.VISIBLE
        }


        return binding.root
    }

    private fun setTextViewColor() {

        changeCalendarModeTextView = binding.waterWeekChangeCalendarTv

        val textData: String = changeCalendarModeTextView.text.toString()
        val builder = SpannableStringBuilder(textData)

        val color1 = ResourcesCompat.getColor(resources, R.color.wp_changeCalendar2, null)
        val setColor1 = ForegroundColorSpan(color1)
        builder.setSpan(setColor1, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        val color2 = ResourcesCompat.getColor(resources, R.color.wp_changeCalendar1, null)
        val setColor2 = ForegroundColorSpan(color2)
        builder.setSpan(setColor2, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        changeCalendarModeTextView.text = builder

    }

    private fun weekCalendar() {
        binding.waterWeekYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
        weekCalendarList()

        binding.waterWeekBackIv.setOnClickListener {
            standardDate = standardDate.minusMonths(1)
            binding.waterWeekYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
            weekCalendarList()
        }

        binding.waterWeekForwardIv.setOnClickListener {
            standardDate = standardDate.plusMonths(1)
            binding.waterWeekYearTv.text = "${yearFromDate(standardDate)}년 ${monthFromDate(standardDate)}월"
            weekCalendarList()
        }
    }

    // 주간 달력
    private fun weekCalendarList() {
        val dayList = dayInMonthArray()
        Log.d("dayList", dayList.toString())
        val dayListManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val dayListAdapter = WaterWeekDayRVAdapter(dayList)
        binding.waterWeekDayListRv.apply {
            layoutManager = dayListManager
            adapter = dayListAdapter
        }

        dayListAdapter.setWeekDayClick(object: WaterWeekDayRVAdapter.DayClickListener{
            override fun onWeekDayClick(formattedDate: String) {
                clickdate = formattedDate
                if (clickdate != "" && clickPotId != 0.toLong()) {
                    // 날짜별 목표, 할 일 api 연동
                    getGoalTodo()
                }
                if (clickdate != "" && clickPotId != 0.toLong()) {
                    binding.waterWeekAddGoalLl.visibility = View.VISIBLE
                }
            }
        })
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
                dayList.add(WeekDate(LocalDate.of(standardDate.year, standardDate.monthValue, i)))
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
        // 월간 주간 변경
        binding.waterWeekChangeCalendarLl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, WaterMonthFragment()).addToBackStack(tag)
                .commitAllowingStateLoss()
        }
    }

    // 목표 리사이클러뷰 연동
//    private fun initGoalRecyclerView() {
//        binding.waterWeekGoalRv.apply {
//            adapter = WaterWeekGoalRVAdapter()
//            layoutManager = LinearLayoutManager(context)
//        }
//    }

    // 화분 리사이클러뷰 연동
//    private fun initPotRecyclerView() {
//        val weekPotManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.waterWeekPotTitleRv.apply {
//            adapter = WaterWeekPotTitleRVAdapter()
//            layoutManager = weekPotManager
//        }
//    }


//    private fun initGardenRecyclerView() {
//
//        getGarden()
//
//        val weekGardenManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.waterWeekGardenTitleRv.apply {
//            adapter = WaterWeekGardenTitleRVAdapter()
//            layoutManager = weekGardenManager
//        }
//    }

//    private fun showDialog(formattedDate: String) {
//        binding.waterWeekAddGoalLl.setOnClickListener {
//            val waterWeekGoalDialog = WaterWeekGoalDialog(binding.root.context, this, formattedDate)
//            waterWeekGoalDialog.show()
//        }
//    }



    // 정원 GET api 연동
    private fun getGarden() {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val gardenService = getRetrofit().create(GardenRetrofitInterfaces::class.java)

        gardenService.getGarden("Bearer $accessToken").enqueue(object: Callback<GardenGetResponse>
        {
            override fun onResponse(call: Call<GardenGetResponse>, response: Response<GardenGetResponse>)
            {
                Log.d("GardenGet/ServerSuccess", response.message())
                Log.d("getGarden", response.body()?.result.toString())
                if(response.isSuccessful) {
                    when(response.code()) {
                        200 -> {
                            Log.d("GardenGet/Success", "GardenGet")

                            // 정원 리사이클러뷰 연동
                            val weekGardenManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            val weekGardenAdapter = response.body()?.result?.let { WaterWeekGardenTitleRVAdapter(it.gardens)}

                            binding.waterWeekGardenTitleRv.apply {
                                adapter = weekGardenAdapter
                                layoutManager = weekGardenManager
                            }

                            weekGardenAdapter?.setGardenClick(object: WaterWeekGardenTitleRVAdapter.GardenClickListener{

                                // 정원 클릭 시
                                override fun onGardenClick(potList: List<PotList>) {

                                    binding.waterWeekGoalRv.adapter = null
//                                    clickdate = ""
                                    clickPotId = 0

                                    if (clickdate != "" && clickPotId != 0.toLong()) {
                                        binding.waterWeekAddGoalLl.visibility = View.VISIBLE
                                    } else {
                                        binding.waterWeekAddGoalLl.visibility = View.INVISIBLE
                                    }

                                    if (potList.isNullOrEmpty()) {
                                        binding.waterWeekNonPotTitleTv.visibility = View.VISIBLE
                                    } else {
                                        binding.waterWeekNonPotTitleTv.visibility = View.INVISIBLE
                                    }

                                    // 화분 리사이클러뷰
                                    val weekPotManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                                    val weekPotAdapter = WaterWeekPotTitleRVAdapter(potList)
                                    binding.waterWeekPotTitleRv.apply {
                                        adapter = weekPotAdapter
                                        layoutManager = weekPotManager
                                    }

                                    // 화분 클릭 시 화분 id 저장
                                    weekPotAdapter.setPotClick(object: WaterWeekPotTitleRVAdapter.PotClickListener{
                                        override fun onPotClick(potId: Long) {
                                            clickPotId = potId
                                            if (clickdate != "" && clickPotId != 0.toLong()) {
                                                // 날짜별 목표, 할 일 api 연동
                                                getGoalTodo()
                                                binding.waterWeekAddGoalLl.visibility = View.VISIBLE
                                            }
                                        }
                                    })
                                }
                            })

                            // 정원이 비어있으면
                            if (response.body()?.result?.gardens?.isEmpty() == true) {
                                binding.itemWaterWeekGardenTitleNonLl.visibility = View.VISIBLE
                            }
                            // 정원이 비어있지 않으면
                            else {
                                binding.itemWaterWeekGardenTitleNonLl.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GardenGetResponse>, t: Throwable) {
                Log.d("GardenGet/Failure", t.message.toString())
            }

        })
    }

    // 목표, 할일 GET api 연동
    private fun getGoalTodo() {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val goalService = getRetrofit().create(GoalRetrofitInterfaces::class.java)

        goalService.getGoalTodo("Bearer $accessToken", clickdate.toString(), clickPotId).enqueue(object: Callback<GoalTodoGetResponse>{
            override fun onResponse(call: Call<GoalTodoGetResponse>, response: Response<GoalTodoGetResponse>) {
                Log.d("GoalTodoGet/ServerSuccess", response.message())
                if (response.isSuccessful) {
                    Log.d("getGoalTodo/Request", clickdate.toString())
                    Log.d("getGoalTodo/Request", clickPotId.toString())
                    Log.d("getGoalTodo", response.body()?.result.toString())

                    if (clickdate != "" || clickPotId != 0.toLong()) {
                        // 목표 리사이클러뷰 연동
                        val goalManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        val goalAdapter = response.body()?.result?.let { WaterWeekGoalRVAdapter(it.goals) }
                        binding.waterWeekGoalRv.apply {
                            adapter = goalAdapter
                            layoutManager = goalManager
                        }

                        goalAdapter?.setGoalAddClick(object: WaterWeekGoalRVAdapter.ItemClickListener{

                            // 할 일 추가 클릭 시 dialog
                            override fun onTodoAddClick(goalName: String, goalId: Long) {
                                val waterWeekGoalTodoDialog = WaterWeekGoalTodoDialog(requireContext(), this@WaterWeekFragment, goalName, goalId, clickdate.toString())
                                waterWeekGoalTodoDialog.show()
                            }

                            // 할 일 클릭 시 dialog
                            override fun onTodoClick2(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String, clickGoalId: Long) {
                                val waterWeekGoalPatchDialog = WaterWeekGoalPatchDialog(requireContext(), this@WaterWeekFragment, clickTodoId, clickTodoTitle, clickTodoDate, clickTodoTime, clickTodoGoalTitle, clickGoalId)
                                waterWeekGoalPatchDialog.show()
                            }

                            override fun onOutlineWaterClick2(doneId: Long, doneBoolean: Boolean) {
                                patchTodoComplete(doneId, TodoPatchCompleteRequest(doneBoolean))
                            }

                            override fun onFillWaterClick2(doneId: Long, doneBoolean: Boolean) {
                                patchTodoComplete(doneId, TodoPatchCompleteRequest(doneBoolean))
                            }

                        })
                    }

                }
            }

            override fun onFailure(call: Call<GoalTodoGetResponse>, t: Throwable) {
                Log.d("getGoalTodo/Failure", t.message.toString())
            }

        })
    }

    // 할 일 완료 수정 api 연동
    private fun patchTodoComplete(doneId: Long, todoPatchCompleteRequest: TodoPatchCompleteRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)

        todoService.patchTodoComplete("Bearer $accessToken", doneId, todoPatchCompleteRequest).enqueue(object: Callback<TodoPatchCompleteResponse>{
            override fun onResponse(call: Call<TodoPatchCompleteResponse>, response: Response<TodoPatchCompleteResponse>) {
                Log.d("TodoCompletePatch/ServerSuccess", response.toString())
                Log.d("TodoCompletePatchRequest", todoPatchCompleteRequest.toString())

                val resp: TodoPatchCompleteResponse? = response.body()
                when(resp?.code) {
                    "200" -> Log.d("TodoCompletePatch/Success", "TodoCompletePatch!!")
                }
            }

            override fun onFailure(call: Call<TodoPatchCompleteResponse>, t: Throwable) {
                Log.d("TodoCompletePatch/Failure", t.message.toString())
            }

        })
    }

    // 확인 클릭 시
    override fun onCompleteClicked() {
        getGoalTodo()
    }

    // 수정 클릭 시
    override fun onPatchClicked() {
        getGoalTodo()
    }

    // 삭제 클릭 시
    override fun onDeleteClicked() {
        getGoalTodo()
    }

}