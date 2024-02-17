package com.example.wantplant.ui.main.water.week

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

class WaterWeekGoalTodoDialog(context: Context, waterWeekInterface: WaterWeekInterface, private var goalName: String, private val goalId: Long, private var todoDate: String) : Dialog(context) {
    private var mBinding : DialogWaterWeekTodoBinding? = null
    private val binding get() = mBinding!!
    private lateinit var todoTime: String
    private var todoTitle: String = ""

    private var waterWeekInterface : WaterWeekInterface? = null

    init {
        this.waterWeekInterface = waterWeekInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterWeekTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogWaterWeekGoalTv.text = goalName
        binding.dialogWaterWeekDateTv.text = todoDate

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

        // 취소 클릭 시
        binding.dialogWaterWeekCancelTv.setOnClickListener {
            dismiss()
        }

        // 완료 클릭 시
        binding.dialogWaterWeekCompleteTv.setOnClickListener {
            var todoTitle = binding.dialogWaterWeekTodoEt.text.toString()

            postTodoAPI(TodoPostRequest(goalId, todoTitle, todoDate, todoTime))

            this.waterWeekInterface?.onCompleteClicked()

            dismiss()
        }
    }

    private fun postTodoAPI(todoPostRequest: TodoPostRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)
        Log.d("TodoPostRequest", todoPostRequest.toString())

        todoService.postTodo("Bearer $accessToken", todoPostRequest).enqueue(object: Callback<TodoPostResponse>
        {
            override fun onResponse(call: Call<TodoPostResponse>, response: Response<TodoPostResponse>) {
                Log.d("TodoPost/ServerSuccess", response.toString())
                Log.d("TodoAdd", response.body()?.result.toString())
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