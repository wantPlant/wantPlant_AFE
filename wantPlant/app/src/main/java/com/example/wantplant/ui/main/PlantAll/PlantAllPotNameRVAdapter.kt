package com.example.wantplant.ui.main.plantall

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemPlantFlowerpotNameBinding

class PlantAllPotNameRVAdapter (private var onPotClicked: (potId: String) -> Unit): RecyclerView.Adapter<PlantAllPotNameRVAdapter.ViewHolder>() {

    var potTitles = listOf<String>() // 화분 이름 리스트(문자열)
    var potIds = listOf<String>() // 화분 아이디 리스트(문자열)

    fun setOnPotClickListener(listener: (potId: String) -> Unit) {
        onPotClicked = listener
    }

    // 뷰 홀더를 생성할 때 호출
    // 아이템 뷰 객채를 만든 후 재활용하기 위해 뷰 홀더에 던져줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlantFlowerpotNameBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                val position = adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener // adapterPosition이 NO_POSITION이 아닌 경우에만 값 반환
                onPotClicked(potIds[position])
            } // ViewHolder의 뷰에 대한 클릭 리스너 설정, 각 아이템을 클릭했을 때 실행될 동작 정의
        }
    }

    // 뷰 홀더에 데이터를 바인딩해줘야 할 때마다(사용자가 화면을 위아래로 스크롤 할 때마다) 호출
    // position -> 리사이클러뷰에서의 인덱스 id
    // 받아온 뷰 홀더에 바인딩해주기 위해 리스트에서 해당 포지션에 위치하는 데이터를 ViewHolder의 bind 함수에 던져줌
    // 포지션 값을 가지고 있기 때문에 클릭 이벤트는 이곳에서 작성
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val potTitle = potTitles[position]
        holder.binding.itemPlantPotNameTv.text = potTitle
    }

    override fun getItemCount(): Int {
        return potTitles.size
    }

    // 아이템 뷰 객체들을 재활용하기 위해 날라가지 않도록 담음
    // 매개변수: 아이템 뷰 객체
    // 뷰 홀더 클래스 상속 받음
    inner class ViewHolder(val binding: ItemPlantFlowerpotNameBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}