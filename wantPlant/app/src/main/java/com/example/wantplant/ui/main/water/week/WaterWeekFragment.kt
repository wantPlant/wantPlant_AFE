package com.example.wantplant.ui.main.water.week

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentWaterWeekBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.water.month.WaterMonthFragment
//import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
//import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
//import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
//import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import java.util.Calendar
import java.util.Date

class WaterWeekFragment : Fragment() {
    private lateinit var binding : FragmentWaterWeekBinding

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterWeekBinding.inflate(layoutInflater)

        // initCalendar()

        clickListener()

        initRecyclerView()

        return binding.root
    }

    /*
private fun initCalendar() {
    calendar.time = Date()
    currentMonth = calendar[Calendar.MONTH]

    val myCalendarViewManager = object : CalendarViewManager {
        override fun bindDataToCalendarView(
            holder: SingleRowCalendarAdapter.CalendarViewHolder,
            date: Date,
            position: Int,
            isSelected: Boolean
        ) {

            val dayOfMonth = com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber(date)
            val dayOfWeek = com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName(date)

            // Set the day of the week in item_day_tv
            (holder.itemView.findViewById<View>(R.id.item_day_tv) as? TextView)?.text = dayOfWeek

            // Set the day of the month in item_date_tv
            (holder.itemView.findViewById<View>(R.id.item_date_tv) as? TextView)?.text = dayOfMonth
        }

        override fun setCalendarViewResourceId(
            position: Int,
            date: Date,
            isSelected: Boolean
        ): Int {
            val cal = Calendar.getInstance()
            cal.time = date
            return if (isSelected)
                R.layout.item_water_week_selected
            else
                R.layout.item_water_week_deselected
        }
    }

    val mySelectionManager = object : CalendarSelectionManager {
        override fun canBeItemSelected(position: Int, date: Date): Boolean {
            val cal = Calendar.getInstance()
            cal.time = date
            Log.d("WaterWeekFragment", "Selected Date: $date")
            return true
        }
    }

    val myCalendarChangesObserver = object : CalendarChangesObserver {
        override fun whenWeekMonthYearChanged(weekNumber: String,monthNumber: String,monthName: String,year: String,date: Date) {
            super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
        }

        override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
            super.whenSelectionChanged(isSelected, position, date)
        }

        override fun whenCalendarScrolled(dx: Int, dy: Int) {
            super.whenCalendarScrolled(dx, dy)
        }

        override fun whenSelectionRestored() {
            super.whenSelectionRestored()
        }

        override fun whenSelectionRefreshed() {
            super.whenSelectionRefreshed()
        }
    }

    binding.waterWeekCalendar.apply {
        calendarViewManager = myCalendarViewManager
        calendarChangesObserver = myCalendarChangesObserver
        calendarSelectionManager = mySelectionManager
        pastDaysCount = 365
        futureDaysCount = 365
        includeCurrentDate = true
        init()
    }
}
*/


    private fun clickListener() {
        binding.waterWeekChangeCalendarLl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, WaterMonthFragment()).addToBackStack(tag)
                .commitAllowingStateLoss()
        }
    }

    private fun initRecyclerView() {
        binding.waterWeekGoalRv.apply {
            adapter = WaterWeekGoalRVAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }
}