package com.example.wantplant.ui.main.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.databinding.FragmentLandingpageBinding

class LandingPageFragment(val imgPage : Int) : Fragment(){
    private lateinit var binding : FragmentLandingpageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLandingpageBinding.inflate(inflater, container, false)

        binding.landingpageIv.setImageResource((imgPage))
        return binding.root
    }

}