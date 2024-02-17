package com.example.wantplant.ui.main.water.week

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
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import com.example.wantplant.databinding.DialogWaterWeekBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.Calendar

class WaterWeekGoalDialog(context: Context, waterWeekInterface: WaterWeekInterface, private var formattedDate: String, private val potId: Long) : Dialog(context) {
    private var mBinding : DialogWaterWeekBinding? = null
    private val binding get() = mBinding!!
    private lateinit var tagTime: String

    private var waterWeekInterface : WaterWeekInterface? = null

    init {
        this.waterWeekInterface = waterWeekInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogWaterWeekDateTv.text = formattedDate

        // 시간 설정하기
        binding.dialogWaterWeekTimeLl.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                when (hourOfDay) {
                    0 -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterWeekTimeTv.text = "00:0${minute}"
                                tagTime = "00:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterWeekTimeTv.text = "00:00"
                                tagTime = "00:00"
                            }

                            else -> {
                                binding.dialogWaterWeekTimeTv.text = "00:${minute}"
                                tagTime = "00:${minute}"
                            }
                        }
                    }
                    in 1..9 -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterWeekTimeTv.text = "0${hourOfDay}:0${minute}"
                                tagTime = "0${hourOfDay}:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterWeekTimeTv.text = "0${hourOfDay}:00"
                                tagTime = "0${hourOfDay}:00"
                            }

                            else -> {
                                binding.dialogWaterWeekTimeTv.text = "0${hourOfDay}:${minute}"
                                tagTime = "0${hourOfDay}:${minute}"
                            }
                        }
                    }
                    else -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterWeekTimeTv.text = "${hourOfDay}:0${minute}"
                                tagTime = "${hourOfDay}:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterWeekTimeTv.text = "${hourOfDay}:00"
                                tagTime = "${hourOfDay}:00"
                            }

                            else -> {
                                binding.dialogWaterWeekTimeTv.text = "${hourOfDay}:${minute}"
                                tagTime = "${hourOfDay}:${minute}"
                            }
                        }
                    }
                }
            }
            TimePickerDialog(context, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }

        // 취소 눌렀을 때
        binding.dialogWaterWeekCancelTv.setOnClickListener {
            dismiss()
        }

        // 확인 눌렀을 때
        binding.dialogWaterWeekCompleteTv.setOnClickListener {
            var goalName = binding.dialogWaterWeekGoalEt.text.toString()
            var todoName = binding.dialogWaterWeekTodoEt.text.toString()
            var todoTime = binding.dialogWaterWeekTimeTv.text.toString()

            postGoalAPI(GoalPostRequest(potId, goalName, TodoList(todoName, formattedDate, todoTime)))

            dismiss()
        }

    }

    // 목표, 할 일 추가 api 연동
    private fun postGoalAPI(goalPostRequest: GoalPostRequest) {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val goalService = getRetrofit().create(GoalRetrofitInterfaces::class.java)

        goalService.postGoalTodo("Bearer $accessToken", goalPostRequest).enqueue(object: Callback<GoalPostResponse>
        {
            override fun onResponse(call: Call<GoalPostResponse>, response: Response<GoalPostResponse>) {
                Log.d("GoalPost/Request", goalPostRequest.toString())
                Log.d("GoalPost/ServerSuccess", response.toString())
                val resp: GoalPostResponse? = response.body()
                Log.d("GoalAdd", response.body()?.result.toString())
                if (resp != null) {
                    waterWeekInterface?.onCompleteClicked()
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