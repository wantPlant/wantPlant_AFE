package com.example.wantplant.ui.main.water.week

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.remote.goal.GoalRetrofitInterfaces
import com.example.wantplant.data.remote.goal.request.GoalPatchRequest
import com.example.wantplant.data.remote.goal.response.GoalPatchResponse
import com.example.wantplant.data.remote.tag.response.TagPatchResponse
import com.example.wantplant.data.remote.todo.TodoRetrofitInterfaces
import com.example.wantplant.data.remote.todo.request.TodoPatchRequest
import com.example.wantplant.data.remote.todo.response.TodoDeleteResponse
import com.example.wantplant.data.remote.todo.response.TodoPatchResponse
import com.example.wantplant.databinding.DialogWaterWeekPatchBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class WaterWeekGoalPatchDialog(context: Context, waterWeekInterface: WaterWeekInterface, val clickTodoId: Long, var clickTodoTitle: String, var clickTodoDate: String, var clickTodoTime: String, var clickTodoGoalTitle: String, var clickGoalId: Long): Dialog(context) {
    private var mBinding : DialogWaterWeekPatchBinding? = null
    private val binding get() = mBinding!!
    private lateinit var todoTime: String
    private lateinit var todoDate: String

    private var waterWeekInterface: WaterWeekInterface? = null

    init {
        this.waterWeekInterface = waterWeekInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterWeekPatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        todoTime = clickTodoTime
        todoDate = clickTodoDate

        // 클릭한 할 일 정보 표시
        binding.dialogWaterWeekGoalTv.setText(clickTodoGoalTitle)
        binding.dialogWaterWeekTodoEt.setText(clickTodoTitle)
        binding.dialogWaterWeekTimeTv.text = clickTodoTime
        binding.dialogWaterWeekDateTv.text = clickTodoDate

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
            TimePickerDialog(context, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
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

        // 완료 클릭 시
        binding.dialogWaterWeekCompleteTv.setOnClickListener {

        }

        // 삭제 클릭 시
        binding.dialogWaterWeekDeleteTv.setOnClickListener {
            deleteTodoAPI(clickTodoId)

            dismiss()
        }

        // 완료 클릭 시
        binding.dialogWaterWeekCompleteTv.setOnClickListener {
            var todoName = binding.dialogWaterWeekTodoEt.text.toString()
            var goalTitle = binding.dialogWaterWeekGoalTv.text.toString()

            patchTodoAPI(clickTodoId, TodoPatchRequest(todoName, todoDate, todoTime))
            patchGoalAPI(GoalPatchRequest(goalTitle))


            dismiss()
        }
    }

    // 할 일 수정 api 연동
    private fun patchTodoAPI(todoId: Long, todoPatchRequest: TodoPatchRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)

        todoService.patchTodo("Bearer $accessToken", todoId, todoPatchRequest).enqueue(object: Callback<TodoPatchResponse> {
            override fun onResponse(call: Call<TodoPatchResponse>, response: Response<TodoPatchResponse>) {
                Log.d("TodoPatch/ServerSuccess", response.toString())
                Log.d("TodoPatchRequest", todoPatchRequest.toString())

                val resp: TodoPatchResponse? = response.body()

                if (resp != null) {
                    waterWeekInterface?.onPatchClicked()
                }

                when(resp?.code) {
                    "200" -> Log.d("TodoPatch/Success", "TodoPatch!!")
                }
            }

            override fun onFailure(call: Call<TodoPatchResponse>, t: Throwable) {
                Log.d("TodoPatch/Failure", t.message.toString())
            }

        })
    }

    // 목표 이름 수정 api 연동
    private fun patchGoalAPI(goalPatchRequest: GoalPatchRequest) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val goalService = getRetrofit().create(GoalRetrofitInterfaces::class.java)

        goalService.patchGoalTitle("Bearer $accessToken", clickGoalId, goalPatchRequest).enqueue(object: Callback<GoalPatchResponse>{
            override fun onResponse(call: Call<GoalPatchResponse>, response: Response<GoalPatchResponse>) {
                Log.d("GoalPatch/ServerSuccess", response.toString())
                Log.d("GoalPatchRequest", goalPatchRequest.toString())
                val resp: GoalPatchResponse? = response.body()
                if (resp != null) {
                    waterWeekInterface?.onPatchClicked()
                }

                when(resp?.code) {
                    "200" -> Log.d("GoalPatch/Success", "TodoPatch!!")
                }
            }

            override fun onFailure(call: Call<GoalPatchResponse>, t: Throwable) {
                Log.d("GoalPatch/Failure", t.message.toString())
            }

        })
    }

    // 할 일 삭제 api 연동
    private fun deleteTodoAPI(todoId: Long) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)

        todoService.deleteTodo("Bearer $accessToken", todoId = todoId).enqueue(object: Callback<TodoDeleteResponse>{
            override fun onResponse(call: Call<TodoDeleteResponse>, response: Response<TodoDeleteResponse>) {
                Log.d("TodoDelete/ServerSuccess", response.toString())
                Log.d("TodoDeleteRequest", todoId.toString())
                val resp: TodoDeleteResponse? = response.body()
                if (resp != null) {
                    waterWeekInterface?.onDeleteClicked()
                }
            }

            override fun onFailure(call: Call<TodoDeleteResponse>, t: Throwable) {
                Log.d("TodoPatch/Failure", t.message.toString())
            }

        })
    }
}