package com.example.wantplant.ui.main.water.week

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.remote.garden.response.GardenGetList
import com.example.wantplant.data.remote.garden.response.PotList
import com.example.wantplant.databinding.ItemWaterWeekGardenTitleBinding

class WaterWeekGardenTitleRVAdapter(private var gardens: List<GardenGetList>): RecyclerView.Adapter<WaterWeekGardenTitleRVAdapter.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

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

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.itemWaterWeekGardenTitleTv.text = gardens[position].name

        if (selectedPosition == position) {
            holder.binding.itemWaterWeekGardenTitleLl.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EDE3CE"))
        } else {
            holder.binding.itemWaterWeekGardenTitleLl.backgroundTintList = null
        }

        // 정원 타이틀 클릭 시
        holder.binding.itemWaterWeekGardenTitleLl.setOnClickListener {

            notifyItemChanged(selectedPosition)

            // 배경 색상 변경
            holder.binding.itemWaterWeekGardenTitleLl.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EDE3CE"))

            mGardenClickListener.onGardenClick(gardens[position].potList)
            selectedPosition = position

            Log.d("gardenClick", gardens[position].potList.toString())
        }
    }

    override fun getItemCount(): Int = gardens.size

}