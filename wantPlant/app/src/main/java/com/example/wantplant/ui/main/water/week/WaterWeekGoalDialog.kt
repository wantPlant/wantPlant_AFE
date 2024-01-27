package com.example.wantplant.ui.main.water.week

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.databinding.DialogWaterWeekBinding
import java.util.Calendar

class WaterWeekGoalDialog(context: Context, waterWeekGoalDialogInterface: WaterWeekGoalDialogInterface, private var formattedDate: String) : Dialog(context) {
    private var mBinding : DialogWaterWeekBinding? = null
    private val binding get() = mBinding!!

    private var waterWeekGoalDialogInterface : WaterWeekGoalDialogInterface? = null

    init {
        this.waterWeekGoalDialogInterface = waterWeekGoalDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogWaterWeekDateTv.text = formattedDate

        binding.dialogWaterWeekTimeLl.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.dialogWaterWeekTimeTv.text = "${hourOfDay}:${minute}"
            }
            TimePickerDialog(context, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
        }

        binding.dialogWaterWeekCancelTv.setOnClickListener {
            this.waterWeekGoalDialogInterface?.onCancelClicked()

            dismiss()
        }

        binding.dialogWaterWeekCompleteTv.setOnClickListener {
            var todoName = binding.dialogWaterWeekTodoEt.text.toString()
            var todoTime = binding.dialogWaterWeekTimeTv.text.toString()

            this.waterWeekGoalDialogInterface?.onCompleteClicked()

            Log.d("todoName", todoName)
            Log.d("todoTime", todoTime)
            Log.d("date", formattedDate)

            dismiss()
            // 여기서 데이터 저장해서 interface로 넘겨주면 될 듯
        }

    }
}