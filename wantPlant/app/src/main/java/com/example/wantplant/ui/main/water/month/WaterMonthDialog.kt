package com.example.wantplant.ui.main.water.month

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
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

class WaterMonthDialog(context: Context, private var formattedDate: String, waterMonthInterface: WaterMonthInterface) : Dialog(context) {
    private var mBinding : DialogWaterMonthBinding? = null
    private val binding get() = mBinding!!
    private var color : TagColor = TagColor.COLOR_1
    private var tagTime: String = ""

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

        binding.dialogWaterMonthColor1Select.visibility = View.VISIBLE

        binding.dialogWaterMonthColor1.setOnClickListener {
            color = TagColor.COLOR_1
            binding.dialogWaterMonthColor1Select.visibility = View.VISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.INVISIBLE
        }

        binding.dialogWaterMonthColor2.setOnClickListener {
            color = TagColor.COLOR_2
            binding.dialogWaterMonthColor1Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.VISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.INVISIBLE
        }

        binding.dialogWaterMonthColor3.setOnClickListener {
            color = TagColor.COLOR_3
            binding.dialogWaterMonthColor1Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.VISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.INVISIBLE
        }

        binding.dialogWaterMonthColor4.setOnClickListener {
            color = TagColor.COLOR_4
            binding.dialogWaterMonthColor1Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.VISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.INVISIBLE
        }

        binding.dialogWaterMonthColor5.setOnClickListener {
            color = TagColor.COLOR_5
            binding.dialogWaterMonthColor1Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.VISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.INVISIBLE
        }

        binding.dialogWaterMonthColor6.setOnClickListener {
            color = TagColor.COLOR_6
            binding.dialogWaterMonthColor1Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.VISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.INVISIBLE
        }

        binding.dialogWaterMonthColor7.setOnClickListener {
            color = TagColor.COLOR_7
            binding.dialogWaterMonthColor1Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.VISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.INVISIBLE
        }

        binding.dialogWaterMonthColor8.setOnClickListener {
            color = TagColor.COLOR_8
            binding.dialogWaterMonthColor1Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor2Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor3Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor4Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor5Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor6Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor7Select.visibility = View.INVISIBLE
            binding.dialogWaterMonthColor8Select.visibility = View.VISIBLE
        }

        // 시간 설정
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

        // 취소 클릭 시
        binding.dialogWaterMonthCancelTv.setOnClickListener {
            dismiss()
        }

        // 완료 클릭 시
        binding.dialogWaterMonthCompleteTv.setOnClickListener {
            var tagName = binding.dialogWaterMonthTodoEt.text.toString()

            Log.d("colorName", color.toString())
            Log.d("tagName", tagName)
            Log.d("tagTime", tagTime)
            Log.d("date", formattedDate)

            if (tagName != "" && tagTime != "" && formattedDate != "") {
                postTagAPI(TagPostRequest(color, tagName, tagTime, formattedDate))

                dismiss()
            }
            else {
                Toast.makeText(context, "에러요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 태그 추가 api 연동
    private fun postTagAPI(tagPostRequest: TagPostRequest) {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val tagService = getRetrofit().create(TagRetrofitInterfaces::class.java)
        Log.d("TagPostRequest", tagPostRequest.toString())

        tagService.postTag("Bearer $accessToken", tagPostRequest).enqueue(object: Callback<TagPostResponse>
        {
            override fun onResponse(call: Call<TagPostResponse>, response: Response<TagPostResponse>) {
                Log.d("TagPost/ServerSuccess", response.toString())
                val resp: TagPostResponse? = response.body()
                Log.d("TagAdd", response.body()?.result.toString())
                if (resp != null) {
                    waterMonthInterface?.clickDialogComplete()
                }
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