package com.example.wantplant.ui.main.water

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.databinding.FragmentWaterBinding

class WaterFragment : Fragment() {
    private lateinit var binding : FragmentWaterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterBinding.inflate(layoutInflater)

        return binding.root
    }
}