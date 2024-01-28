package com.example.wantplant.ui.main.garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.data.local.ImageItem
import com.example.wantplant.databinding.ItemGardenPotImageBinding

class GardenPotRVAdapter: RecyclerView.Adapter<GardenPotRVAdapter.ViewHolder>() {
    val profileImgData = arrayOf(
        ImageItem(R.drawable.rectangle_2),
        ImageItem(R.drawable.rectangle_2),
        ImageItem(R.drawable.rectangle_2),
        ImageItem(R.drawable.rectangle_2),
        ImageItem(R.drawable.rectangle_2)
    )

    inner class ViewHolder(val binding: ItemGardenPotImageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): GardenPotRVAdapter.ViewHolder {
        val binding: ItemGardenPotImageBinding = ItemGardenPotImageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GardenPotRVAdapter.ViewHolder, position: Int) {
        holder.binding.itemGardenPotIv.setImageResource(profileImgData[position].imageResourceId)
    }

    override fun getItemCount(): Int = profileImgData.size
}