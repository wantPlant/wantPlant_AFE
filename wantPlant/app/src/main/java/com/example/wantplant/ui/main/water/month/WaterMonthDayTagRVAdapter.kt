package com.example.wantplant.ui.main.water.month

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.remote.tag.response.TagMonthGetResult
import com.example.wantplant.databinding.ItemMonthDayTagBinding

class WaterMonthDayTagRVAdapter(private val tag: List<TagMonthGetResult>?): RecyclerView.Adapter<WaterMonthDayTagRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMonthDayTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemMonthDayTagTodo.text = tag?.get(position)?.tagName.toString()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemMonthDayTagBinding = ItemMonthDayTagBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tag?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


}