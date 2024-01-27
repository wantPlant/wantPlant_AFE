package com.example.wantplant.ui.main.water.week

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentWaterWeekBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.month.WaterMonthFragment

class WaterWeekFragment : Fragment(), WaterWeekGoalDialogInterface, WaterWeekRVAdapter.onDateClickedListener {
    private lateinit var binding : FragmentWaterWeekBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterWeekBinding.inflate(layoutInflater)

        val weekListManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val weekListAdapter = WaterWeekRVAdapter(this)
        
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

        initPotRecyclerView()

        initGardenRecyclerView()

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

    private fun initPotRecyclerView() {
        val weekPotManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.waterWeekPotTitleRv.apply {
            adapter = WaterWeekPotTitleRVAdapter()
            layoutManager = weekPotManager
        }
    }

    private fun initGardenRecyclerView() {
        val weekGardenManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.waterWeekGardenTitleRv.apply {
            adapter = WaterWeekGardenTitleRVAdapter()
            layoutManager = weekGardenManager
        }
    }

    private fun showDialog(formattedDate: String) {
        binding.waterWeekAddGoalLl.setOnClickListener {
            val waterWeekGoalDialog = WaterWeekGoalDialog(binding.root.context, this, formattedDate)
            waterWeekGoalDialog.show()
        }
    }

    override fun onCancelClicked() {

    }

    override fun onCompleteClicked() {

    }

    override fun onDateClicked(formattedDate: String) {
        Log.d("날짜가 여기까지 왔을까요....", "Selected Date: $formattedDate")
        showDialog(formattedDate)
    }

}