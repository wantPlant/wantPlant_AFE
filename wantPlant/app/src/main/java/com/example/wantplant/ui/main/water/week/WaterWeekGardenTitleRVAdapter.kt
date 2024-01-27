package com.example.wantplant.ui.main.water.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterWeekGardenTitleBinding

class WaterWeekGardenTitleRVAdapter: RecyclerView.Adapter<WaterWeekGardenTitleRVAdapter.ViewHolder>() {

    val gardenTitle = arrayOf( "정원1", "정원2", "정원3", "정원4", "정원5" )

    inner class ViewHolder(val binding: ItemWaterWeekGardenTitleBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekGardenTitleRVAdapter.ViewHolder {
        val binding : ItemWaterWeekGardenTitleBinding = ItemWaterWeekGardenTitleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemWaterWeekGardenTitleTv.text = gardenTitle[position]
    }

    override fun getItemCount(): Int = gardenTitle.size

}