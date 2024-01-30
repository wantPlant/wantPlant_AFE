package com.example.wantplant.ui.main.garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemGardenTitleBinding

class GardenGardenRVAdapter: RecyclerView.Adapter<GardenGardenRVAdapter.ViewHolder>() {

    var gardenTitles = listOf<String>()

    inner class ViewHolder(val binding: ItemGardenTitleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemGardenTitleBinding = ItemGardenTitleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = gardenTitles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemGardenTitleTv.text = gardenTitles[position]
    }

}