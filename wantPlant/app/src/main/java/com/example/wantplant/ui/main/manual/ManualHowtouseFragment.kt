package com.example.wantplant.ui.main.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.databinding.FragmentManualHowtouseBinding

class ManualHowtouseFragment : Fragment() {
    private lateinit var binding : FragmentManualHowtouseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManualHowtouseBinding.inflate(layoutInflater)

        return binding.root
    }
}