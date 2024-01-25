package com.example.wantplant.ui.main.water.week

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentWaterWeekBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.month.WaterMonthFragment
import java.util.Calendar
import java.util.Date

class WaterWeekFragment : Fragment() {
    private lateinit var binding : FragmentWaterWeekBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterWeekBinding.inflate(layoutInflater)

        val weekListManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val weekListAdapter = WaterWeekRVAdapter()

        binding.waterWeekCalendarRv.apply {
            layoutManager = weekListManager
            adapter = weekListAdapter

            scrollToPosition(Int.MAX_VALUE/2)
        }

        binding.waterWeekCalendarRv.suppressLayout(true)

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.waterWeekCalendarRv)

        onClickListener()

        initGoalRecyclerView()

        return binding.root
    }

    private fun onClickListener() {
        binding.waterWeekChangeCalendarLl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, WaterMonthFragment()).addToBackStack(tag)
                .commitAllowingStateLoss()
        }
    }

    private fun initGoalRecyclerView() {
        binding.waterWeekGoalRv.apply {
            adapter = WaterWeekGoalRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }
}