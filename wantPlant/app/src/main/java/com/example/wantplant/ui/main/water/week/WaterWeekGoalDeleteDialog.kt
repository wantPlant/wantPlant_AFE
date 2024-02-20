package com.example.wantplant.ui.main.water.week

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.wantplant.data.remote.goal.GoalRetrofitInterfaces
import com.example.wantplant.data.remote.goal.response.GoalDeleteResponse
import com.example.wantplant.databinding.DialogDeleteGoalBinding
import com.example.wantplant.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WaterWeekGoalDeleteDialog(context: Context, waterWeekInterface: WaterWeekInterface, val goalTitle: String, val goalId: Long): Dialog(context) {
    private var mBinding: DialogDeleteGoalBinding? = null
    private val binding get() = mBinding!!

    private var waterWeekInterface: WaterWeekInterface? = null

    init {
        this.waterWeekInterface = waterWeekInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogDeleteGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogDeleteGoalNameTv.text = goalTitle

        binding.dialogDeleteGoalCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.dialogDeleteGoalCompleteBtn.setOnClickListener {
            deleteGoal(goalId)
            dismiss()
        }
    }

    private fun deleteGoal(goalId: Long) {
        val sharedPref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val accessToken = sharedPref?.getString("accessToken", "")

        val goalService = getRetrofit().create(GoalRetrofitInterfaces::class.java)

        goalService.deleteGoal("Bearer $accessToken", goalId).enqueue(object:
            Callback<GoalDeleteResponse> {
            override fun onResponse(call: Call<GoalDeleteResponse>, response: Response<GoalDeleteResponse>) {
                Log.d("GoalDelete/ServerSuccess", response.toString())
                Log.d("GoalDeleteRequest", goalId.toString())

                val resp: GoalDeleteResponse? = response.body()
                if (resp != null) {
                    waterWeekInterface?.onDeleteClicked()
                }
            }

            override fun onFailure(call: Call<GoalDeleteResponse>, t: Throwable) {
                Log.d("GoalDelete/Failure", t.message.toString())
            }

        })
    }
}