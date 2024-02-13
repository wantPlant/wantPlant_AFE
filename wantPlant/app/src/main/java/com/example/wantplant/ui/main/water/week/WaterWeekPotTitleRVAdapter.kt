package com.example.wantplant.ui.main.water.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.remote.garden.response.PotList
import com.example.wantplant.databinding.ItemWaterWeekPotTitleBinding

class WaterWeekPotTitleRVAdapter(private var potList: List<PotList>): RecyclerView.Adapter<WaterWeekPotTitleRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWaterWeekPotTitleBinding): RecyclerView.ViewHolder(binding.root)

    interface PotClickListener {
        fun onPotClick(potId: Long)
    }

    private lateinit var mPotClickListener: PotClickListener

    fun setPotClick(potClickListener: PotClickListener) {
        mPotClickListener = potClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekPotTitleRVAdapter.ViewHolder {
        val binding: ItemWaterWeekPotTitleBinding = ItemWaterWeekPotTitleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekPotTitleRVAdapter.ViewHolder, position: Int) {
        holder.binding.itemWaterWeekPotTitleTv.text = potList[position].potName

        holder.binding.itemWaterWeekPotTitleTv.setOnClickListener {
            mPotClickListener.onPotClick(potList[position].potId)
        }
    }

    override fun getItemCount(): Int = potList.size
}