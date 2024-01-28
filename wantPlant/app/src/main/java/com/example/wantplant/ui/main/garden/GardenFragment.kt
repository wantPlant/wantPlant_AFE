package com.example.wantplant.ui.main.garden

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentGardenBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.plant.PlantActivity
import com.example.wantplant.ui.main.selectgarden.SelectGardenActivity

class GardenFragment : Fragment() {
    private lateinit var binding : FragmentGardenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGardenBinding.inflate(layoutInflater)

        initGardenRecyclerView()

        initPotRecyclerView()

        onClickListener()

        return binding.root
    }

    private fun initGardenRecyclerView() {
        val gardenManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.gardenGardenRv.apply {
            adapter = GardenGardenRVAdapter()
            layoutManager = gardenManager
        }
    }

    private fun initPotRecyclerView() {
        val potManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.gardenPotRv.apply {
            adapter = GardenPotRVAdapter()
            layoutManager = potManager
        }
    }

    private fun onClickListener() {

        // 정원 만들기를 눌렀을 때
        binding.gardenAddGardenTv.setOnClickListener {
            val intent = Intent(activity, SelectGardenActivity::class.java)
            startActivity(intent)
        }

        // 화분 심기를 눌렀을 때
        binding.gardenAddPotLl.setOnClickListener {
            val intent = Intent(activity, PlantActivity::class.java)
            startActivity(intent)
        }
    }

}