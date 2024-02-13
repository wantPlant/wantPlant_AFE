package com.example.wantplant.ui.main.water.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.remote.garden.response.GardenGetList
import com.example.wantplant.data.remote.garden.response.PotList
import com.example.wantplant.databinding.ItemWaterWeekGardenTitleBinding

class WaterWeekGardenTitleRVAdapter(private var gardens: List<GardenGetList>): RecyclerView.Adapter<WaterWeekGardenTitleRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWaterWeekGardenTitleBinding): RecyclerView.ViewHolder(binding.root)

    interface GardenClickListener {
        fun onGardenClick(potList: List<PotList>)
    }

    private lateinit var mGardenClickListener: GardenClickListener

    fun setGardenClick(gardenClickListener: GardenClickListener) {
        mGardenClickListener = gardenClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekGardenTitleRVAdapter.ViewHolder {
        val binding : ItemWaterWeekGardenTitleBinding = ItemWaterWeekGardenTitleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemWaterWeekGardenTitleTv.text = gardens[position].name

        holder.binding.itemWaterWeekGardenTitleLl.setOnClickListener {
            mGardenClickListener.onGardenClick(gardens[position].potList)
        }
    }

    override fun getItemCount(): Int = gardens.size

}