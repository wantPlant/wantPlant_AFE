package com.example.wantplant.ui.main.plantAll

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.remote.goal.GoalRetrofitInterfaces
import com.example.wantplant.data.remote.goal.request.GoalPostRequest
import com.example.wantplant.data.remote.goal.request.TodoList
import com.example.wantplant.data.remote.goal.response.GoalPostResponse
import com.example.wantplant.databinding.DialogWaterWeekBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PlantAllGoalDialog(context: Context, plantAllInterface: PlantAllInterface, private val potId: Long): Dialog(context) {
    private var mBinding : DialogWaterWeekBinding? = null
    private val binding get() = mBinding!!
    private lateinit var todoTime: String
    private lateinit var todoDate: String

    private var plantAllInterface: PlantAllInterface? = null

    init {
        this.plantAllInterface = plantAllInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 시간 설정하기
        binding.dialogWaterWeekTimeLl.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                when (hourOfDay) {
                    0 -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterWeekTimeTv.text = "00:0${minute}"
                                todoTime = "00:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterWeekTimeTv.text = "00:00"
                                todoTime = "00:00"
                            }

                            else -> {
                                binding.dialogWaterWeekTimeTv.text = "00:${minute}"
                                todoTime = "00:${minute}"
                            }
                        }
                    }
                    in 1..9 -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterWeekTimeTv.text = "0${hourOfDay}:0${minute}"
                                todoTime = "0${hourOfDay}:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterWeekTimeTv.text = "0${hourOfDay}:00"
                                todoTime = "0${hourOfDay}:00"
                            }

                            else -> {
                                binding.dialogWaterWeekTimeTv.text = "0${hourOfDay}:${minute}"
                                todoTime = "0${hourOfDay}:${minute}"
                            }
                        }
                    }
                    else -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterWeekTimeTv.text = "${hourOfDay}:0${minute}"
                                todoTime = "${hourOfDay}:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterWeekTimeTv.text = "${hourOfDay}:00"
                                todoTime = "${hourOfDay}:00"
                            }

                            else -> {
                                binding.dialogWaterWeekTimeTv.text = "${hourOfDay}:${minute}"
                                todoTime = "${hourOfDay}:${minute}"
                            }
                        }
                    }
                }
            }
            TimePickerDialog(context, TimePickerDialog.THEME_HOLO_LIGHT, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }

        binding.dialogWaterWeekDateLl.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                if (month in 1..9) {
                    if (dayOfMonth in 1..9) {
                        binding.dialogWaterWeekDateTv.text = "${year}-0${month+1}-0${dayOfMonth}"
                        todoDate = "${year}-0${month+1}-0${dayOfMonth}"
                    }
                    else {
                        binding.dialogWaterWeekDateTv.text = "${year}-0${month+1}-${dayOfMonth}"
                        todoDate = "${year}-0${month+1}-${dayOfMonth}"
                    }
                }
                else {
                    if (dayOfMonth in 1..9) {
                        binding.dialogWaterWeekDateTv.text = "${year}-${month+1}-0${dayOfMonth}"
                        todoDate = "${year}-${month+1}-0${dayOfMonth}"
                    }
                    else {
                        binding.dialogWaterWeekDateTv.text = "${year}-${month+1}-${dayOfMonth}"
                        todoDate = "${year}-${month+1}-${dayOfMonth}"
                    }
                }
            }

            DatePickerDialog(context, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }


        // 취소 눌렀을 때
        binding.dialogWaterWeekCancelTv.setOnClickListener {
            dismiss()
        }

        // 확인 눌렀을 때
        binding.dialogWaterWeekCompleteTv.setOnClickListener {
            var goalName = binding.dialogWaterWeekGoalEt.text.toString()
            var todoName = binding.dialogWaterWeekTodoEt.text.toString()
            todoTime = binding.dialogWaterWeekTimeTv.text.toString()
            todoDate = binding.dialogWaterWeekDateTv.text.toString()

            postGoalAPI(GoalPostRequest(potId, goalName, TodoList(todoName, todoDate, todoTime)))

            dismiss()
        }

    }

    // 목표, 할 일 추가 api 연동
    private fun postGoalAPI(goalPostRequest: GoalPostRequest) {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val goalService = getRetrofit().create(GoalRetrofitInterfaces::class.java)

        goalService.postGoalTodo("Bearer $accessToken", goalPostRequest).enqueue(object: Callback<GoalPostResponse> {
            override fun onResponse(call: Call<GoalPostResponse>, response: Response<GoalPostResponse>) {
                Log.d("GoalPost/Request", goalPostRequest.toString())
                Log.d("GoalPost/ServerSuccess", response.toString())
                val resp: GoalPostResponse? = response.body()
                Log.d("GoalAdd", response.body()?.result.toString())
                if (resp != null) {
                    plantAllInterface?.onCompleteClicked()
                }
                when(resp?.code) {
                    "200" -> Log.d("GoalAdd/Success", "GoalAdd!!")
                }
            }

            override fun onFailure(call: Call<GoalPostResponse>, t: Throwable) {
                Log.d("GoalAdd/Failure", t.message.toString())
            }

        })
    }
}