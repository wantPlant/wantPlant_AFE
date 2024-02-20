package com.example.wantplant.ui.main.water.week

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.data.remote.garden.response.PotList
import com.example.wantplant.databinding.ItemWaterWeekPotTitleBinding

class WaterWeekPotTitleRVAdapter(private var potList: List<PotList>): RecyclerView.Adapter<WaterWeekPotTitleRVAdapter.ViewHolder>() {

    private var selectedPosition: Int = 0

    interface PotClickListener {
        fun onPotClick(potId: Long)
    }

    private var mPotClickListener: PotClickListener? = null

    fun setPotClick(potClickListener: PotClickListener) {
        mPotClickListener = potClickListener
    }

    inner class ViewHolder(val binding: ItemWaterWeekPotTitleBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekPotTitleRVAdapter.ViewHolder {
        val binding: ItemWaterWeekPotTitleBinding = ItemWaterWeekPotTitleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekPotTitleRVAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
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

            holder.binding.itemWaterWeekPotTitleTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemWaterWeekPotTitleTv.setTypeface(null, Typeface.BOLD)
            holder.binding.itemWaterWeekPotTitleTv.setTextColor(Color.BLACK)

            mPotClickListener?.onPotClick(potList[position].potId)
            Log.d("화분아이디", potList[position].potId.toString())

            selectedPosition = position
        }
    }

    override fun getItemCount(): Int = potList.size
}