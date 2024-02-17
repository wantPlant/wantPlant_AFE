package com.example.wantplant.ui.main.plant

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.local.Goal
import com.example.wantplant.data.local.TodoResult
import com.example.wantplant.data.remote.pot.request.PotPostRequest
import com.example.wantplant.data.remote.pot.request.Todo
import com.example.wantplant.data.remote.todo.TodoRetrofitInterfaces
import com.example.wantplant.data.remote.todo.request.TodoPostRequest
import com.example.wantplant.data.remote.todo.response.TodoPostResponse
import com.example.wantplant.databinding.DialogPlantBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PlantDialog(context: Context, plantDialogInterface: PlantDialogInterface) : Dialog(context) {
    private var mBinding : DialogPlantBinding? = null
    private val binding get() = mBinding!!
    private var plantDialogInterface : PlantDialogInterface? = null

    init {
        this.plantDialogInterface = plantDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogPlantMonthTimeLl.setOnClickListener{
            val cal = Calendar.getInstance()
            val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.dialogPlantMonthTimeTv.text = "${hourOfDay}:${minute}"
            }

            TimePickerDialog(context, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
        }

        binding.dialogPlantMonthDateLl.setOnClickListener{
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                binding.dialogPlantMonthDateTv.text = "${year}.${month}.${day}"
            }

            DatePickerDialog(context, data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.dialogPlantCancelBtn.setOnClickListener{
            this.plantDialogInterface?.onCancelClicked()

            dismiss()
        }

        binding.dialogPlantCompleteBtn.setOnClickListener{
            var goalName = binding.dialogPlantGoalEt.text.toString()
            var todoName = binding.dialogPlantGoalEt.text.toString()
            var todoTime = binding.dialogPlantMonthTimeTv.text.toString()
            var todoDate = binding.dialogPlantMonthDateTv.text.toString()

            this.plantDialogInterface?.onCompleteClicked()

            Log.d("goalName", goalName)
            Log.d("todoName", todoName)
            Log.d("todoTime", todoTime)
            Log.d("todoDate", todoDate)

            // 여기서 목표를 생성하고 해당 목표에 투두를 추가함


            dismiss()
        }
    }

    private fun postTodoAPI(todoPostRequest: TodoPostRequest) {
        /*val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)
        Log.d("TodoPostRequest", todoPostRequest.toString())

        todoService.postTodo(todoPostRequest).enqueue(object: Callback<TodoPostResponse> {
            override fun onResponse(call: Call<TodoPostResponse>, response: Response<TodoPostResponse>) {
                Log.d("TodoPost/ServerSuccess", response.toString())

                val resp: TodoPostResponse? = response.body()
                Log.d("TodoAdd", "code: ${resp?.message}")

                when(resp?.code) {
                    "200" -> Log.d("TodoAdd/Success", "TodoAdd!!")
                }
            }

            override fun onFailure(call: Call<TodoPostResponse>, t: Throwable) {
                Log.d("TodoAdd/Failure", t.message.toString())
            }
        })*/
    }
}