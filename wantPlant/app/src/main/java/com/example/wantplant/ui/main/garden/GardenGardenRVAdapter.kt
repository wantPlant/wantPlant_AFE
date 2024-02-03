package com.example.wantplant.ui.main.garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemGardenTitleBinding

class GardenGardenRVAdapter(private val onGardenClicked: (gardenId: String) -> Unit) : RecyclerView.Adapter<GardenGardenRVAdapter.ViewHolder>() {
    var gardenTitles = listOf<String>()
    var gardenIds = listOf<String>()

    inner class ViewHolder(val binding: ItemGardenTitleBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder 생성 시, 클릭 리스너 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGardenTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                val position = adapterPosition.takeIf { it != NO_POSITION }
                    ?: return@setOnClickListener
                onGardenClicked(gardenIds[position])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gardenTitle = gardenTitles[position]
        // 아이템 뷰에 데이터를 바인딩
        // 예를 들어, 정원의 이름을 텍스트 뷰에 설정한다든지
        holder.binding.itemGardenTitleTv.text = gardenTitle
    }

    override fun getItemCount(): Int = gardenTitles.size
}

