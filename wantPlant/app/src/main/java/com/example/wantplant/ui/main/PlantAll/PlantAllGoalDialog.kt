package com.example.wantplant.ui.main.plantall

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.remote.todo.TodoRetrofitInterfaces
import com.example.wantplant.data.remote.todo.request.TodoPostRequest
import com.example.wantplant.data.remote.todo.response.TodoPostResponse
import com.example.wantplant.databinding.DialogWaterWeekTodoBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PlantAllGoalDialog(context: Context, plantAllInterface: PlantAllInterface, private var goalName: String, private val goalId: Long) : Dialog(context) {
    private var mBinding : DialogWaterWeekTodoBinding? = null
    private val binding get() = mBinding!!
    private lateinit var todoTime: String // 할 일 시간
    private lateinit var todoDate: String // 할 일 날짜

    private var plantAllInterface : PlantAllInterface? = null

    init {
        this.plantAllInterface = plantAllInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterWeekTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogWaterWeekGoalTv.text = goalName

        // 시간 설정
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
            TimePickerDialog(context, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }

        // 날짜 설정
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

        // 취소 클릭 시
        binding.dialogWaterWeekCancelTv.setOnClickListener {
            dismiss()
        }

        // 완료 클릭 시
        binding.dialogWaterWeekCompleteTv.setOnClickListener {
            var todoTitle = binding.dialogWaterWeekTodoEt.text.toString()

            postTodoAPI(TodoPostRequest(goalId, todoTitle, todoDate, todoTime))
            // 해당 목표에 수정한 할 일 제목, 날짜, 시간 저장

            this.plantAllInterface?.onCompleteClicked()

            dismiss()
        }
    }

    // 할 일 수정
    private fun postTodoAPI(todoPostRequest: TodoPostRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")
        Log.d("Retrofit 할 일 수정", "사용된 액세스 토큰: Bearer $accessToken")

        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)
        Log.d("TodoPostRequest", todoPostRequest.toString())

        todoService.postTodo("Bearer $accessToken", todoPostRequest).enqueue(object: Callback<TodoPostResponse>
        {
            override fun onResponse(call: Call<TodoPostResponse>, response: Response<TodoPostResponse>) {
                Log.d("TodoPost/ServerSuccess", response.toString())
                Log.d("TodAdd", response.body()?.result.toString())

                when(response.body()?.code) {
                    "200" -> Log.d("TodoAdd/Success", "TodoAdd!!")
                }
            }

            override fun onFailure(call: Call<TodoPostResponse>, t: Throwable) {
                Log.d("TodoAdd/Failure", t.message.toString())
            }
        })
    }
}