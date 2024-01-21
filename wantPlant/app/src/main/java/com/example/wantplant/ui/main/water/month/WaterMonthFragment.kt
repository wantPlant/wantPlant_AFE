package com.example.wantplant.ui.main.water.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentWaterMonthBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.week.WaterWeekFragment

class WaterMonthFragment : Fragment() {
    private lateinit var binding: FragmentWaterMonthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterMonthBinding.inflate(layoutInflater)

        // binding.waterCalendarRv는 각 월을 나타낼 리스트이며 가로로 전환하기 위해 LinearLayoutManager.HORIZONTAL을 씀
        val monthListManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = WaterMonthRVAdapter()

        binding.waterMonthCalendarRv.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter

            // 리스트를 item의 위치를 지정한 곳에서 시작
            scrollToPosition(Int.MAX_VALUE/2)
        }

        // PagerSnapHelper(): 한 항목씩 스크롤 됨
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.waterMonthCalendarRv)

        binding.waterMonthChangeCalendarLl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, WaterWeekFragment()).addToBackStack(tag)
                .commitAllowingStateLoss()
        }
        return binding.root
    }
}