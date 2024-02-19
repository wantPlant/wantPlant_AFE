package com.example.wantplant.ui.main.plantAll

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.data.local.Pot
import com.example.wantplant.databinding.ItemWaterWeekPotTitleBinding
import com.example.wantplant.ui.main.water.week.WaterWeekPotTitleRVAdapter

class PlantAllPotRVAdapter(private var potList: List<Pot>) : RecyclerView.Adapter<PlantAllPotRVAdapter.ViewHolder>() {

    private var selectedPosition: Int = 0

    inner class ViewHolder(val binding: ItemWaterWeekPotTitleBinding): RecyclerView.ViewHolder(binding.root)

    interface PotClickListener {
        fun onPotClick(potInfo: Pot)
    }

    private lateinit var mPotClickListener: PotClickListener

    fun setPotClick(potClickListener: PotClickListener) {
        mPotClickListener = potClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWaterWeekPotTitleBinding = ItemWaterWeekPotTitleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = potList.size

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.itemWaterWeekPotTitleTv.text = potList[position].potName

        if (selectedPosition == position) {
            holder.binding.itemWaterWeekPotTitleTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemWaterWeekPotTitleTv.setTypeface(null, Typeface.BOLD)
            holder.binding.itemWaterWeekPotTitleTv.setTextColor(Color.BLACK)
        } else {
            holder.binding.itemWaterWeekPotTitleTv.setBackgroundResource(R.drawable.border_nonfill_graystroke_15radius)
            holder.binding.itemWaterWeekPotTitleTv.setTypeface(null, Typeface.NORMAL)
            holder.binding.itemWaterWeekPotTitleTv.setTextColor(Color.GRAY)
        }

        // 화분 클릭 시
        holder.binding.itemWaterWeekPotTitleTv.setOnClickListener {
            notifyItemChanged(selectedPosition)

            mPotClickListener.onPotClick(potList[position])

            holder.binding.itemWaterWeekPotTitleTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemWaterWeekPotTitleTv.setTypeface(null, Typeface.BOLD)
            holder.binding.itemWaterWeekPotTitleTv.setTextColor(Color.BLACK)

            selectedPosition = position
        }
    }
}