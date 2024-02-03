package com.example.wantplant.ui.main.plant

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.databinding.DialogPlantBinding
import java.util.Calendar

class PlantDialog(context: Context, PlantDialogInterface: PlantDialogInterface) : Dialog(context) {
    private var mBinding : DialogPlantBinding? = null
    private val binding get() = mBinding!!
    private var PlantDialogInterface : PlantDialogInterface? = null

    init {
        this.PlantDialogInterface = PlantDialogInterface
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
            this.PlantDialogInterface?.onCancelClicked()

            dismiss()
        }

        binding.dialogPlantCompleteBtn.setOnClickListener{
            var goalName = binding.dialogPlantGoalEt.text.toString()
            var goalTime = binding.dialogPlantMonthTimeTv.text.toString()
            var goalDate = binding.dialogPlantMonthDateTv.text.toString()

            this.PlantDialogInterface?.onCompleteClicked()

            Log.d("goalName", goalName)
            Log.d("goalTime", goalTime)
            Log.d("goalDate", goalDate)

            dismiss()
        }
    }
}