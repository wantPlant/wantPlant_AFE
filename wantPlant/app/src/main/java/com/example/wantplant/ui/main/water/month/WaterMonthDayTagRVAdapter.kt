package com.example.wantplant.ui.main.water.month

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemMonthDayTagBinding
import java.util.Date

class WaterMonthDayTagRVAdapter: RecyclerView.Adapter<WaterMonthDayTagRVAdapter.ViewHolder>() {

    private val data = arrayOf( "할일1", "할일2", "얏호얏호얏호얏호", "할일!!" )

    inner class ViewHolder(val binding: ItemMonthDayTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemMonthDayTagTodo.text = data[position]
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemMonthDayTagBinding = ItemMonthDayTagBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


}