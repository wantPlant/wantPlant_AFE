package com.example.wantplant.ui.main.water.month

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.wantplant.databinding.DialogWaterMonthBinding

class WaterMonthDialog(context: Context, waterMonthDialogInterface: WaterMonthDialogInterface) : Dialog(context) {
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

        binding.dialogWaterMonthCancelTv.setOnClickListener {
            this.waterMonthDialogInterface?.onCancelClicked()

            dismiss()
        }
        binding.dialogWaterMonthCompleteTv.setOnClickListener {
            this.waterMonthDialogInterface?.onCompleteClicked()

            // 여기서 데이터 저장해서 interface로 넘겨주면 될 듯
        }
    }
}