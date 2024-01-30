package com.example.wantplant.ui.main.water.month

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.remote.tag.TagRetrofitInterfaces
import com.example.wantplant.data.remote.tag.request.TagPostRequest
import com.example.wantplant.data.remote.tag.response.TagColor
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import com.example.wantplant.databinding.DialogWaterMonthBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class WaterMonthDialog(context: Context, private var formattedDate: String) : Dialog(context) {
    private var mBinding : DialogWaterMonthBinding? = null
    private val binding get() = mBinding!!
    private var color : TagColor? = null

    private var waterMonthDialogInterface : WaterMonthDialogInterface? = null

    init {
        this.waterMonthDialogInterface = waterMonthDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterMonthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogGardenMonthDateTv.text = formattedDate

        binding.dialogGardenMonthColor1.setOnClickListener {
            color = TagColor.RED
        }

        binding.dialogGardenMonthColor2.setOnClickListener {
            color = TagColor.BLUE
        }

        binding.dialogGardenMonthColor3.setOnClickListener {
            color = TagColor.GREEN
        }

        binding.dialogGardenMonthColor4.setOnClickListener {
            color = TagColor.ORANGE
        }

        binding.dialogGardenMonthColor5.setOnClickListener {
            color = TagColor.INDIGO
        }

        binding.dialogGardenMonthColor6.setOnClickListener {
            color = TagColor.YELLOW
        }

        binding.dialogGardenMonthColor7.setOnClickListener {
            color = TagColor.YELLOW
        }

        binding.dialogGardenMonthColor8.setOnClickListener {
            color = TagColor.YELLOW
        }

        binding.dialogGardenMonthTimeLl.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.dialogGardenMonthTimeTv.text = "${hourOfDay}:${minute}"
            }
            TimePickerDialog(context, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
        }

        binding.dialogWaterMonthCancelTv.setOnClickListener {
            this.waterMonthDialogInterface?.onCancelClicked()

            dismiss()
        }

        binding.dialogWaterMonthCompleteTv.setOnClickListener {
            var tagName = binding.dialogGardenMonthTodoEt.text.toString()
            var tagTime = binding.dialogGardenMonthTimeTv.text.toString()

            this.waterMonthDialogInterface?.onCompleteClicked()

            Log.d("colorName", color.toString())
            Log.d("tagName", tagName)
            Log.d("tagTime", tagTime)
            Log.d("date", formattedDate)

            postTagAPI(TagPostRequest(color!!, tagName, formattedDate))

            dismiss()
        }
    }

    // 태그 추가 api 연동
    private fun postTagAPI(request: TagPostRequest) {
        val tagService = getRetrofit().create(TagRetrofitInterfaces::class.java)
        Log.d("request", request.toString())

        tagService.postTag(request).enqueue(object: Callback<TagPostResponse>
        {
            override fun onResponse(call: Call<TagPostResponse>, response: Response<TagPostResponse>) {
                Log.d("TagPost/Success", response.toString())
                val resp: TagPostResponse = response.body()!!
                when(resp.code) {
                    "200" -> Log.d("TagAdd/Success", "TagAdd!!")
                }
            }

            override fun onFailure(call: Call<TagPostResponse>, t: Throwable) {
                Log.d("TagAdd/Failure", t.message.toString())
            }
        })
    }
}