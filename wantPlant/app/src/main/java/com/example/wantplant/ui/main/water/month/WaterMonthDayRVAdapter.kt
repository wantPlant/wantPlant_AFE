package com.example.wantplant.ui.main.water.month

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.MonthDate
import com.example.wantplant.data.remote.tag.response.TagMonthGetResult
import com.example.wantplant.databinding.ItemWaterMonthDayBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WaterMonthDayRVAdapter(private val dayList: MutableList<MonthDate>): RecyclerView.Adapter<WaterMonthDayRVAdapter.ViewHolder>(){

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: ItemWaterMonthDayBinding): RecyclerView.ViewHolder(binding.root)

    interface ItemClickListener {
        fun onDayClick(formattedTagDate: String)
        fun onTagClick2(tag: TagMonthGetResult)
    }

    private lateinit var mItemClickListener: ItemClickListener

    fun setDayClick(itemClickListener: ItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWaterMonthDayBinding = ItemWaterMonthDayBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val context = holder.binding.root.context

        // 해당 날을 클릭 했을 때
        holder.binding.itemDayLayout.setOnClickListener {

            if (dayList[position].tag?.size ?:0 > 3) {
                val waterMonthWarningDialog = WaterMonthWarningDialog(context)
                waterMonthWarningDialog.show()
            }
            else if(dayList[position].tag?.size == null || dayList[position].tag?.size!! <= 3){
                val formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                Log.d("날짜", dayList[position].date!!.format(formattedDate))

                mItemClickListener.onDayClick(dayList[position].date!!.format(formattedDate))
            }
            notifyItemChanged(selectedPosition)

            // 채운 하트 표시
            holder.binding.waterMonthDaySelectIv.visibility = View.VISIBLE

            selectedPosition = position

        }

        if (selectedPosition == position) {
            holder.binding.waterMonthDaySelectIv.visibility = View.VISIBLE
        } else {
            holder.binding.waterMonthDaySelectIv.visibility = View.INVISIBLE
        }

        // 이번 달 날짜인 것 체크
        if (dayList[position].date != null) {
            holder.binding.itemDayText.text = dayList[position].date?.dayOfMonth.toString()
            val tagListManager = LinearLayoutManager(context)
            val tagListAdapter = WaterMonthDayTagRVAdapter(dayList[position].tag)
            holder.binding.waterMonthDayRv.apply {
                layoutManager = tagListManager
                adapter = tagListAdapter
            }
            tagListAdapter.setTagClick(object: WaterMonthDayTagRVAdapter.TagClickListener{
                override fun onTagClick(tag: TagMonthGetResult) {
                    mItemClickListener.onTagClick2(tag)
                }

            })
        }
        else {
            holder.binding.itemDayText.text = ""
        }

        // 오늘 날짜에 하트 표시
        if (dayList[position].date == LocalDate.now()) {
            holder.binding.waterMonthDayTodayIv.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return dayList.size
    }

}