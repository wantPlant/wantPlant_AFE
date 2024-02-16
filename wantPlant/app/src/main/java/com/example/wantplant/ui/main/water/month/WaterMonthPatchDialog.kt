package com.example.wantplant.ui.main.water.month

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.wantplant.R
import com.example.wantplant.data.remote.tag.TagRetrofitInterfaces
import com.example.wantplant.data.remote.tag.request.TagPatchRequest
import com.example.wantplant.data.remote.tag.response.TagColor
import com.example.wantplant.data.remote.tag.response.TagDeleteResponse
import com.example.wantplant.data.remote.tag.response.TagMonthGetResult
import com.example.wantplant.data.remote.tag.response.TagPatchResponse
import com.example.wantplant.databinding.DialogWaterMonthPatchBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class WaterMonthPatchDialog(context: Context, private var tag: TagMonthGetResult, waterMonthInterface: WaterMonthInterface) : Dialog(context) {
    private var mBinding : DialogWaterMonthPatchBinding? = null
    private val binding get() = mBinding!!
    private lateinit var color : TagColor
    private lateinit var tagTime: String
    private lateinit var tagDate: String

    private var waterMonthInterface: WaterMonthInterface? = null

    init {
        this.waterMonthInterface = waterMonthInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterMonthPatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tagTime = tag.tagTime
        tagDate = tag.date

        // 클릭한 태그 정보 표시
        binding.dialogWaterMonthTimeTv.text = tag.tagTime
        binding.dialogWaterMonthDateTv.text = tag.date
        binding.dialogWaterMonthTodoEt.setText(tag.tagName)
        color = tag.tagColor

        when (color) {
            TagColor.COLOR_1 -> {
                binding.dialogWaterMonthColor1Select.visibility = View.VISIBLE
            }
            TagColor.COLOR_2 -> {
                binding.dialogWaterMonthColor2Select.visibility = View.VISIBLE
            }
            TagColor.COLOR_3 -> {
                binding.dialogWaterMonthColor3Select.visibility = View.VISIBLE
            }
            TagColor.COLOR_4 -> {
                binding.dialogWaterMonthColor4Select.visibility = View.VISIBLE
            }
            TagColor.COLOR_5 -> {
                binding.dialogWaterMonthColor5Select.visibility = View.VISIBLE
            }
            TagColor.COLOR_6 -> {
                binding.dialogWaterMonthColor6Select.visibility = View.VISIBLE
            }
            TagColor.COLOR_7 -> {
                binding.dialogWaterMonthColor7Select.visibility = View.VISIBLE
            }
            TagColor.COLOR_8 -> {
                binding.dialogWaterMonthColor8Select.visibility = View.VISIBLE
            }
        }

        // 태그 색깔 선택
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

        binding.dialogWaterMonthDateLl.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                if (month in 1..9) {
                    if (dayOfMonth in 1..9) {
                        binding.dialogWaterMonthDateTv.text = "${year}-0${month+1}-0${dayOfMonth}"
                        tagDate = "${year}-0${month+1}-0${dayOfMonth}"
                    }
                    else {
                        binding.dialogWaterMonthDateTv.text = "${year}-0${month+1}-${dayOfMonth}"
                        tagDate = "${year}-0${month+1}-${dayOfMonth}"
                    }
                }
                else {
                    if (dayOfMonth in 1..9) {
                        binding.dialogWaterMonthDateTv.text = "${year}-${month+1}-0${dayOfMonth}"
                        tagDate = "${year}-${month+1}-0${dayOfMonth}"
                    }
                    else {
                        binding.dialogWaterMonthDateTv.text = "${year}-${month+1}-${dayOfMonth}"
                        tagDate = "${year}-${month+1}-${dayOfMonth}"
                    }
                }
            }

            DatePickerDialog(context, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.dialogWaterMonthCompleteTv.setOnClickListener {
            var tagName = binding.dialogWaterMonthTodoEt.text.toString()

            patchTagAPI(TagPatchRequest(tag.id, color, tagName, tagTime, tagDate))

            dismiss()
        }

        binding.dialogWaterMonthDeleteTv.setOnClickListener {
            deleteTagAPI(tag.id)

            dismiss()
        }
    }

    // 태그 수정 api 연동
    private fun patchTagAPI(tagPatchRequest: TagPatchRequest) {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val tagService = getRetrofit().create(TagRetrofitInterfaces::class.java)

        tagService.patchTag("Bearer $accessToken", tagPatchRequest).enqueue(object: Callback<TagPatchResponse>
        {
            override fun onResponse(call: Call<TagPatchResponse>, response: Response<TagPatchResponse>) {
                Log.d("TagPatch/ServerSuccess", response.toString())
                Log.d("TagPatchRequest", tagPatchRequest.toString())
                val resp: TagPatchResponse? = response.body()
                if (resp != null) {
                    waterMonthInterface?.clickDialogPatch()
                }
                when(resp?.code) {
                    "200" -> Log.d("TagPatch/Success", "TagPatch!!")
                }
            }

            override fun onFailure(call: Call<TagPatchResponse>, t: Throwable) {
                Log.d("TagPatch/Failure", t.message.toString())
            }

        })
    }

    // 태그 삭제 api 연동
    private fun deleteTagAPI(tagId: Long) {

        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val tagService = getRetrofit().create(TagRetrofitInterfaces::class.java)

        tagService.deleteTag("Bearer $accessToken", tagId = tagId).enqueue(object : Callback<TagDeleteResponse>{
            override fun onResponse(call: Call<TagDeleteResponse>, response: Response<TagDeleteResponse>) {
                Log.d("TagDelete/ServerSuccess", response.toString())
                Log.d("TagDeleteRequest", tagId.toString())
                val resp: TagDeleteResponse? = response.body()
                if (resp != null) {
                    waterMonthInterface?.clickDialogDelete()
                }
            }

            override fun onFailure(call: Call<TagDeleteResponse>, t: Throwable) {
                Log.d("TagPatch/Failure", t.message.toString())
            }

        })
    }
}