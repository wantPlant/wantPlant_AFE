package com.example.wantplant.ui.main.plant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wantplant.R
import com.example.wantplant.databinding.ActivityPlantBinding
import com.example.wantplant.ui.main.MainActivity

class PlantActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlantBinding.inflate(layoutInflater)

        onClickListener()

        setContentView(binding.root)
    }

    private fun onClickListener() {
        binding.plantBackIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}