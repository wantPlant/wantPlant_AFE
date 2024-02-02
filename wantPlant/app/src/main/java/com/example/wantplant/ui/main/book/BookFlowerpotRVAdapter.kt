package com.example.wantplant.ui.main.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wantplant.data.local.CompletedPot
import com.example.wantplant.databinding.ItemBookFlowerpotBinding

// 매개변수: 데이터리스트
// adapter 클래스 상속 받음 (ViewHolder 넣어주어야 함)
class BookFlowerpotRVAdapter(private val completedPots: List<CompletedPot>) : RecyclerView.Adapter<BookFlowerpotRVAdapter.ViewHolder>() {
    // 아이템 뷰 객체들을 재활용하기 위해 날라가지 않도록 담음
    // 매개변수: 아이템 뷰 객체
    // 뷰 홀더 클래스 상속 받음
    inner class ViewHolder(val binding: ItemBookFlowerpotBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    // 뷰 홀더를 생성할 때 호출
    // 아이템 뷰 객채를 만든 후 재활용하기 위해 뷰 홀더에 던져줌
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBookFlowerpotBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding) // 아이템 뷰 객체 던짐
    }

    // 뷰 홀더에 데이터를 바인딩해줘야 할 때마다(사용자가 화면을 위아래로 스크롤 할 때마다) 호출
    // position -> 리사이클러뷰에서의 인덱스 id
    // 받아온 뷰 홀더에 바인딩해주기 위해 리스트에서 해당 포지션에 위치하는 데이터를 ViewHolder의 bind 함수에 던져줌
    // 포지션 값을 가지고 있기 때문에 클릭 이벤트는 이곳에서 작성
    override fun onBindViewHolder(holder: BookFlowerpotRVAdapter.ViewHolder, position: Int) {
        // Glide를 사용하여 이미지 로드 및 표시
        Glide.with(holder.binding.itemFlowerpotPlantIv.context) // ImageView에 이미지 표시
            .load(completedPots[position].potImageUrl) // 이미지 URL 지정
            .override(316, 447) // 원하는 가로, 세로 사이즈 입력
            .into(holder.binding.itemFlowerpotPlantIv)

        holder.binding.itemFlowerpotNameIv.text = completedPots[position].potName
        holder.binding.itemFlowerpotPeriodTv.text = "${completedPots[position].startAt} ~ ${completedPots[position].completedAt}"
    }
    override fun getItemCount(): Int {
        return completedPots.size
    }
}