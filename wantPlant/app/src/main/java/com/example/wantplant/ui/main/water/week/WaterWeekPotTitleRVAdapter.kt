package com.example.wantplant.ui.main.water.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterWeekPotTitleBinding

class WaterWeekPotTitleRVAdapter: RecyclerView.Adapter<WaterWeekPotTitleRVAdapter.ViewHolder>() {

    val potTitle = arrayOf( "화분1", "화분2", "화분3", "화분4", "화분5" )

    inner class ViewHolder(val binding: ItemWaterWeekPotTitleBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekPotTitleRVAdapter.ViewHolder {
        val binding: ItemWaterWeekPotTitleBinding = ItemWaterWeekPotTitleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekPotTitleRVAdapter.ViewHolder, position: Int) {
        holder.binding.itemWaterWeekPotTitleTv.text = potTitle[position]
    }

    override fun getItemCount(): Int = potTitle.size
}