package com.example.wantplant.ui.main.plant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.databinding.ActivityPlantBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.week.WaterWeekGoalRVAdapter

class PlantActivity : AppCompatActivity(), PlantDialogInterface {
    private var mBinding : ActivityPlantBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPlantBinding.inflate(layoutInflater)

        onClickListener()

        setContentView(binding.root)

        binding.plantWaterWeekGoalRv.apply {
            adapter = WaterWeekGoalRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun onClickListener() {
        binding.plantBackIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCompleteClicked() {}

    override fun onCancelClicked() {}

}