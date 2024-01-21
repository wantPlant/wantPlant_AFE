package com.example.wantplant.ui.main.water.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemWaterWeekTodoBinding

class WaterWeekTodoRVAdapter(private val data: List<String>, private val parentPos: Int) : RecyclerView.Adapter<WaterWeekTodoRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): WaterWeekTodoRVAdapter.ViewHolder {
        val binding = ItemWaterWeekTodoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekTodoRVAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: ItemWaterWeekTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemTodoTitleTv.text = data[position]
        }
    }

}