package com.example.wantplant.ui.main.garden

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.data.local.GardenData
import com.example.wantplant.databinding.ItemGardenTitleBinding

class GardenGardenRVAdapter(var gardens: List<GardenData>) : RecyclerView.Adapter<GardenGardenRVAdapter.ViewHolder>() {
//    var gardenTitles = listOf<String>()
//    var gardenIds = listOf<String>()
//    var currentGardenId: String? = null

    private var selectedPosition: Int = 0

    interface GardenClickListener {
        fun onGardenTitleClick(clickedGardenId: Long, clickedGardenTitle: String, clickedGardenDes: String)
    }

    private lateinit var mGardenClickListener: GardenClickListener

    fun setGardenTitleClick(gardenClickListener: GardenClickListener) {
        mGardenClickListener = gardenClickListener
    }

    inner class ViewHolder(val binding: ItemGardenTitleBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder 생성 시, 클릭 리스너 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGardenTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
//        return ViewHolder(binding).apply {
//            itemView.setOnClickListener {
//                val position = adapterPosition.takeIf { it != NO_POSITION }
//                    ?: return@setOnClickListener
//                currentGardenId = gardenIds[position] // 현재 선택된 정원의 ID를 업데이트
//                Log.d("현재 선택된 정원 이름", "${currentGardenId}")
//                onGardenClicked(gardenIds[position])
//            }
//        }
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
//        val gardenTitle = gardenTitles[position]
//        val gardenId = gardenIds[position]

        if (selectedPosition == position) {
            holder.binding.itemGardenTitleTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemGardenTitleTv.setTypeface(null, Typeface.BOLD)
            holder.binding.itemGardenTitleTv.setTextColor(Color.BLACK)
        } else {
            holder.binding.itemGardenTitleTv.setBackgroundResource(R.drawable.border_nonfill_graystroke_15radius)
            holder.binding.itemGardenTitleTv.setTypeface(null, Typeface.NORMAL)
            holder.binding.itemGardenTitleTv.setTextColor(Color.GRAY)
        }

        // 정원 이름 클릭 시
        holder.binding.itemGardenTitleTv.setOnClickListener {
            notifyItemChanged(selectedPosition)

            holder.binding.itemGardenTitleTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemGardenTitleTv.setTypeface(null, Typeface.BOLD)
            holder.binding.itemGardenTitleTv.setTextColor(Color.BLACK)

            mGardenClickListener.onGardenTitleClick(gardens[position].gardenId, gardens[position].name, gardens[position].description)

            selectedPosition = position
        }


        holder.binding.itemGardenTitleTv.text = gardens[position].name

    }

    override fun getItemCount(): Int {

        return gardens.size
    }

}

