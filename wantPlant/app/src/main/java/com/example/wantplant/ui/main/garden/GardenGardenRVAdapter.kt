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
//        Log.d("정원 이름", "${gardenTitle}")
//        Log.d("Retrofit 정원 이름", "Position: $position, Garden Title: $gardenTitle, Garden ID: $gardenId")

    }

    override fun getItemCount(): Int {
//        val count = minOf(gardenTitles.size, gardenIds.size)
//        Log.d("Retrofit 정원 이름", "Item Count: $count")
        return gardens.size
    }

//    fun setData(gardenTitles: List<String>, gardenIds: List<String>) {
//        this.gardenTitles = gardenTitles
//        this.gardenIds = gardenIds
//        Log.d("Retrofit 정원 이름", "setData called with gardenTitles: $gardenTitles, gardenIds: $gardenIds")
//        notifyDataSetChanged()  // 데이터가 변경되었음을 알림
//    }

//    fun getCurrentGardenName(): String {
//        // 'currentGardenId'가 null이 아니라면 해당 ID의 정원의 이름을 반환하고, null이라면 빈 문자열 반환
//        return if (currentGardenId != null) {
//            // 'gardenIds'에서 'currentGardenId'와 일치하는 ID를 가진 정원의 인덱스를 찾음
//            val index = gardenIds.indexOf(currentGardenId)
//            // 찾은 인덱스를 사용하여 'gardenTitles'에서 정원의 이름을 가져옵니다. 만약 찾지 못했다면 빈 문자열 반환
//            if (index != -1) gardenTitles[index] else ""
//        } else {
//            ""
//        }
//    }
}

