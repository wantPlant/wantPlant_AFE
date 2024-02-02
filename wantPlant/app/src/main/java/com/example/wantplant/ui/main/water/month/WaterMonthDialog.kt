package com.example.wantplant.ui.main.water.month

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.R
import com.example.wantplant.data.remote.tag.TagRetrofitInterfaces
import com.example.wantplant.data.remote.tag.request.TagPostRequest
import com.example.wantplant.data.remote.tag.response.TagColor
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import com.example.wantplant.databinding.DialogWaterMonthBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class WaterMonthDialog(context: Context, private var formattedDate: String, waterMonthInterface: WaterMonthInterface) : Dialog(context) {
    private var mBinding : DialogWaterMonthBinding? = null
    private val binding get() = mBinding!!
    private var color : TagColor = TagColor.COLOR_1
    private lateinit var tagTime: String

    private var waterMonthInterface : WaterMonthInterface? = null

    init {
        this.waterMonthInterface = waterMonthInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterMonthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogWaterMonthDateTv.text = formattedDate

        binding.dialogWaterMonthColor1.setOnClickListener {
            color = TagColor.COLOR_1
        }

        binding.dialogWaterMonthColor2.setOnClickListener {
            color = TagColor.COLOR_2
        }

        binding.dialogWaterMonthColor3.setOnClickListener {
            color = TagColor.COLOR_3
        }

        binding.dialogWaterMonthColor4.setOnClickListener {
            color = TagColor.COLOR_4
        }

        binding.dialogWaterMonthColor5.setOnClickListener {
            color = TagColor.COLOR_5
        }

        binding.dialogWaterMonthColor6.setOnClickListener {
            color = TagColor.COLOR_6
        }

        binding.dialogWaterMonthColor7.setOnClickListener {
            color = TagColor.COLOR_7
        }

        binding.dialogWaterMonthColor8.setOnClickListener {
            color = TagColor.COLOR_8
        }

        binding.dialogWaterMonthTimeLl.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                when (hourOfDay) {
                    0 -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterMonthTimeTv.text = "00:0${minute}"
                                tagTime = "00:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterMonthTimeTv.text = "00:00"
                                tagTime = "00:00"
                            }

                            else -> {
                                binding.dialogWaterMonthTimeTv.text = "00:${minute}"
                                tagTime = "00:${minute}"
                            }
                        }
                    }
                    in 1..9 -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterMonthTimeTv.text = "0${hourOfDay}:0${minute}"
                                tagTime = "0${hourOfDay}:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterMonthTimeTv.text = "0${hourOfDay}:00"
                                tagTime = "0${hourOfDay}:00"
                            }

                            else -> {
                                binding.dialogWaterMonthTimeTv.text = "0${hourOfDay}:${minute}"
                                tagTime = "0${hourOfDay}:${minute}"
                            }
                        }
                    }
                    else -> {
                        when (minute) {
                            in 1..9 -> {
                                binding.dialogWaterMonthTimeTv.text = "${hourOfDay}:0${minute}"
                                tagTime = "${hourOfDay}:0${minute}"
                            }

                            0 -> {
                                binding.dialogWaterMonthTimeTv.text = "${hourOfDay}:00"
                                tagTime = "${hourOfDay}:00"
                            }

                            else -> {
                                binding.dialogWaterMonthTimeTv.text = "${hourOfDay}:${minute}"
                                tagTime = "${hourOfDay}:${minute}"
                            }
                        }
                    }
                }
            }
            TimePickerDialog(context, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()
        }

        binding.dialogWaterMonthCancelTv.setOnClickListener {

            dismiss()
        }

        binding.dialogWaterMonthCompleteTv.setOnClickListener {
            var tagName = binding.dialogWaterMonthTodoEt.text.toString()

            Log.d("colorName", color.toString())
            Log.d("tagName", tagName)
            Log.d("tagTime", tagTime)
            Log.d("date", formattedDate)

            postTagAPI(TagPostRequest(color, tagName, tagTime, formattedDate))

            this.waterMonthInterface?.clickDialogComplete()

            dismiss()
        }
    }

    // 태그 추가 api 연동
    private fun postTagAPI(tagPostRequest: TagPostRequest) {
        val tagService = getRetrofit().create(TagRetrofitInterfaces::class.java)
        Log.d("TagPostRequest", tagPostRequest.toString())

        tagService.postTag(tagPostRequest).enqueue(object: Callback<TagPostResponse>
        {
            override fun onResponse(call: Call<TagPostResponse>, response: Response<TagPostResponse>) {
                Log.d("TagPost/ServerSuccess", response.toString())
                val resp: TagPostResponse? = response.body()
                Log.d("TagAdd", "code: ${resp?.message}")
                when(resp?.code) {
                    "200" -> Log.d("TagAdd/Success", "TagAdd!!")
                }
            }

            override fun onFailure(call: Call<TagPostResponse>, t: Throwable) {
                Log.d("TagAdd/Failure", t.message.toString())
            }
        })
    }

}