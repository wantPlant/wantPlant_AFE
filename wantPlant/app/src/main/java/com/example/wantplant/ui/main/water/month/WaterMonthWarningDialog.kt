package com.example.wantplant.ui.main.water.month

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.wantplant.databinding.DialogWaterMonthWarningBinding

class WaterMonthWarningDialog(context: Context) : Dialog(context) {
    private var mBinding : DialogWaterMonthWarningBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogWaterMonthWarningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogWaterMonthCompleteTv.setOnClickListener {
            dismiss()
        }
    }
}