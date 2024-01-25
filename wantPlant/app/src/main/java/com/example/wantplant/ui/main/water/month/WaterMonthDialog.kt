package com.example.wantplant.ui.main.water.month

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.wantplant.databinding.DialogWaterMonthBinding
import java.util.Calendar

class WaterMonthDialog(context: Context, waterMonthDialogInterface: WaterMonthDialogInterface, private var formattedDate: String) : Dialog(context) {
    private var mBinding : DialogWaterMonthBinding? = null
    private val binding get() = mBinding!!

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

            Log.d("tagName", tagName)
            Log.d("tagTime", tagTime)
            Log.d("date", formattedDate)

            dismiss()
            // 여기서 데이터 저장해서 interface로 넘겨주면 될 듯
        }
    }
}