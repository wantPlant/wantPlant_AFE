package com.example.wantplant.ui.main.plant

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.local.TodoResponse
import com.example.wantplant.data.local.TodoResult
import com.example.wantplant.data.remote.todo.TodoRetrofitInterfaces
import com.example.wantplant.data.remote.todo.request.TodoPatchRequest
import com.example.wantplant.data.remote.todo.response.TodoPatchResponse
import com.example.wantplant.databinding.DialogPlantBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PlantPatchDialog(context: Context, private var todo: TodoResult, plantDialogInterface: PlantDialogInterface) : Dialog(context) {
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
            var todoName = binding.dialogPlantGoalEt.text.toString()
            var todoTime = binding.dialogPlantMonthTimeTv.text.toString()
            var todoDate = binding.dialogPlantMonthDateTv.text.toString()

            Log.d("todoName", todoName)
            Log.d("todoTime", todoTime)
            Log.d("todoDate", todoDate)

            patchTodoAPI(TodoPatchRequest(todoName, todoTime, todoDate))

            this.plantDialogInterface?.onCompleteClicked()

            dismiss()
        }
    }

    private fun getTodoAPI() {
        val retrofit = getRetrofit() // 브라우저 창 열기
        val api = retrofit.create(TodoRetrofitInterfaces::class.java) // 어떤 주소로 들어감 (요청 X)
        val call = api.getTodo(todo.id) // 정원 당 화분 리스트 조회

        // 입력한 주소 중 하나로 연결 시도
        call.enqueue(object : Callback<TodoResponse> {
            override fun onResponse(call: Call<TodoResponse>, response: Response<TodoResponse>) {
                if (response.isSuccessful) {
                    Log.d("Retrofit 목표", "성공 ${api.getTodo(todo.id)}")
                } else {
                    Log.d("Retrofit 목표", "실패 ${response.errorBody()}") // 응답 실패 시의 처리
                }
            }

            override fun onFailure(call: Call<TodoResponse>, t: Throwable) {
                Log.d("Retrofit 목표", "실패 $t") // 요청 실패 시의 처리
            }
        })
    }

    private fun patchTodoAPI(todoPatchRequest: TodoPatchRequest) {
        val todoService = getRetrofit().create(TodoRetrofitInterfaces::class.java)

        todoService.patchTodo(todo.id, todoPatchRequest).enqueue(object: Callback<TodoPatchResponse> {
            override fun onResponse(call: Call<TodoPatchResponse>, response: Response<TodoPatchResponse>) {
                Log.d("TodoPatch/ServerSuccess", response.toString())
                Log.d("TodoPatchRequest", todoPatchRequest.toString())
                val resp: TodoPatchResponse? = response.body()
                when(resp?.code) {
                    "200" -> Log.d("TodoPatch/Success", "TodoPatch!!")
                }
            }

            override fun onFailure(call: Call<TodoPatchResponse>, t: Throwable) {
                Log.d("TodoPatch/Failure", t.message.toString())
            }
        })
    }
}