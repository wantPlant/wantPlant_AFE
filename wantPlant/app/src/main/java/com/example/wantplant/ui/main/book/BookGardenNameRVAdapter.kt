package com.example.wantplant.ui.main.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.databinding.ItemBookGardenNameBinding

class BookGardenNameRVAdapter (private var onGardenClicked: (gardenId: String) -> Unit) :
    RecyclerView.Adapter<BookGardenNameRVAdapter.ViewHolder>() {

    var gardenTitles = listOf<String>()
    var gardenIds = listOf<String>()

    fun setOnGardenClickListener(listener: (gardenId: String) -> Unit) {
        onGardenClicked = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookGardenNameRVAdapter.ViewHolder {
        val binding = ItemBookGardenNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                val position = adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener // adapterPosition이 NO_POSITION이 아닌 경우에만 값 반환
                onGardenClicked(gardenIds[position])
            }
        }
    }

    // 뷰 홀더에 데이터를 바인딩해줘야 할 때마다(사용자가 화면을 위아래로 스크롤 할 때마다) 호출
    // position -> 리사이클러뷰에서의 인덱스 id
    // 받아온 뷰 홀더에 바인딩해주기 위해 리스트에서 해당 포지션에 위치하는 데이터를 ViewHolder의 bind 함수에 던져줌
    // 포지션 값을 가지고 있기 때문에 클릭 이벤트는 이곳에서 작성
    override fun onBindViewHolder(holder: BookGardenNameRVAdapter.ViewHolder, position: Int) {
        val gardenTitle = gardenTitles[position]
        // 아이템 뷰에 데이터를 바인딩
        // 예를 들어, 정원의 이름을 텍스트 뷰에 설정한다든지
        holder.binding.itemBookGardenNameTv.text = gardenTitle
    }

    override fun getItemCount(): Int {
        return gardenTitles.size
    }

    // 아이템 뷰 객체들을 재활용하기 위해 날라가지 않도록 담음
    inner class ViewHolder(val binding: ItemBookGardenNameBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                onGardenClicked(gardenIds[position])
            }
        }
    }
}