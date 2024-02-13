package com.example.wantplant.ui.main.garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.data.local.ImageItem
import com.example.wantplant.data.local.Pot
import com.example.wantplant.databinding.ItemGardenPotImageBinding
import com.bumptech.glide.Glide


class GardenPotRVAdapter(private val pots: List<Pot>) : RecyclerView.Adapter<GardenPotRVAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemGardenPotImageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemGardenPotImageBinding = ItemGardenPotImageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.itemGardenPotIv.context)
            .load(pots[position].potImageUrl)
            .override(316, 447) // 원하는 가로, 세로 사이즈를 입력하세요.
            .into(holder.binding.itemGardenPotIv)
    }

    override fun getItemCount(): Int = pots.size
}

