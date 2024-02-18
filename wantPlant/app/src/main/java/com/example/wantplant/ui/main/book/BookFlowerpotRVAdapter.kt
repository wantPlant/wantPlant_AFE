package com.example.wantplant.ui.main.book

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wantplant.data.local.CompletedPot
import com.example.wantplant.databinding.ItemBookFlowerpotBinding

class BookFlowerpotRVAdapter(private val completedPots: List<CompletedPot>) : RecyclerView.Adapter<BookFlowerpotRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBookFlowerpotBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Log.d("완료된 화분", "성공")
            // Glide를 사용하여 이미지 로드 및 표시
            Glide.with(binding.itemFlowerpotPlantIv.context) // ImageView에 이미지 표시
                .load(completedPots[position].potImageUrl) // 이미지 URL 지정
                .override(316, 447) // 원하는 가로, 세로 사이즈 입력
                .into(binding.itemFlowerpotPlantIv) // 로드한 이미지를 특정 ImageView에 표시

            binding.itemFlowerpotNameIv.text = completedPots[position].potName
            // itemFlowerpotNameIv에 화분 이름 표시

            binding.itemFlowerpotPeriodTv.text =
                "${completedPots[position].startAt} ~ ${completedPots[position].completedAt}"
            // itemFlowerpotPeriodTv에 기간(시작 시간 ~ 완료 시간) 표시

        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBookFlowerpotBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding) // 아이템 뷰 객체 던짐
    }

    override fun onBindViewHolder(holder: BookFlowerpotRVAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return completedPots.size
    }
}