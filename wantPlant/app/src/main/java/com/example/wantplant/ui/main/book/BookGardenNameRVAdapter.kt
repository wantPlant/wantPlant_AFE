package com.example.wantplant.ui.main.book

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.databinding.ItemBookGardenNameBinding

class BookGardenNameRVAdapter (private var onGardenClicked: (gardenId: String) -> Unit) :
    RecyclerView.Adapter<BookGardenNameRVAdapter.ViewHolder>() {
    // onGardenClicked - 클릭된 정원의 ID를 처리하는 함수 (gardenId 문자열을 입력 받고 아무 결과값을 반환하지 않음)

    var gardenTitles = listOf<String>() // 정원 이름 리스트(문자열)
    var gardenIds = listOf<String>() // 정원 아이디 리스트(문자열)
    var currentGardenId: String? = null

    fun setOnGardenClickListener(listener: (gardenId: String) -> Unit) {
        onGardenClicked = listener
    }

    /*
    // 리사이클러뷰 어댑터 외부에서 클릭 이벤트를 처리하기 위한 인터페이스
    interface SetOnItemClickListener {
        fun onGardenNameClick(gardenId: String)
    } // 클릭 이벤트를 처리하기 위한 콜백 인터페이스 정의

    // 어뎁터 외부의 프레그먼트에서 리스너 객체를 던져주면 됨
    private lateinit var myItemClickListener: SetOnItemClickListener
    // 클릭 이벤트를 처리하기 위한 콜백을 저장하기 위한 변수

    fun setItemClickListener(setOnItemClickListener: SetOnItemClickListener){
        myItemClickListener = setOnItemClickListener
    }
    // 외부에서 클릭 이벤트를 처리할 콜백 객체를 설정하는 메서드 정의
    // 외부에서 구현된 SetOnItemClickListener 인터페이스를 구현한 객체를 전달하여 클릭 이벤트 처리
    */

    // 뷰 홀더를 생성할 때 호출
    // 아이템 뷰 객채를 만든 후 재활용하기 위해 뷰 홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookGardenNameRVAdapter.ViewHolder {
        val binding = ItemBookGardenNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                val position = adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener // adapterPosition이 NO_POSITION이 아닌 경우(유효한 위치에 있을 경우)에만 값 반환
                currentGardenId = gardenIds[position] // 현재 선택된 정원의 ID를 업데이트
                Log.d("현재 선택된 정원 이름", "${currentGardenId}")
                onGardenClicked(gardenIds[position])
            } // ViewHolder의 뷰에 대한 클릭 리스너 설정, 각 아이템을 클릭했을 때 실행될 동작 정의
        }
    } // 각 아이템을 클릭했을 때 해당 아이템의 위치를 기반으로 클릭된 정원의 ID 처리

    private var selectedGardenId: String? = null // 클릭된 정원의 ID를 저장할 변수

    fun setSelectedGardenId(gardenId: String) {
        selectedGardenId = gardenId
        notifyDataSetChanged() // 변경된 데이터를 RecyclerView에 적용
    }

    // 뷰 홀더에 데이터를 바인딩해줘야 할 때마다(사용자가 화면을 위아래로 스크롤 할 때마다) 호출
    // position -> 리사이클러뷰에서의 인덱스 id
    // 받아온 뷰 홀더에 바인딩해주기 위해 리스트에서 해당 포지션에 위치하는 데이터를 ViewHolder의 bind 함수에 던져줌
    // 포지션 값을 가지고 있기 때문에 클릭 이벤트는 이곳에서 작성
    override fun onBindViewHolder(holder: BookGardenNameRVAdapter.ViewHolder, position: Int) {
        val gardenTitle = gardenTitles[position]
        val gardenId = gardenIds[position]
        // 아이템 뷰에 데이터를 바인딩
        // 예를 들어, 정원의 이름을 텍스트 뷰에 설정한다든지
        holder.binding.itemBookGardenNameTv.text = gardenTitle
        Log.d("정원 이름", "${gardenTitle}")
        Log.d("Retrofit 정원 이름", "Position: $position, Garden Title: $gardenTitle, Garden ID: $gardenId")

        // 클릭된 정원의 ID와 현재 아이템의 정원 ID가 일치하는지 확인하여 배경을 변경
        if (gardenId == selectedGardenId) {
            // 클릭된 정원의 ID와 현재 아이템의 정원 ID가 일치하면 초록 테두리가 있는 배경으로 변경
            holder.binding.root.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
            holder.binding.itemBookGardenNameTv.setTextColor(Color.parseColor("#616161"))
        } else {
            // 그렇지 않으면 회색 테두리가 있는 배경으로 변경
            holder.binding.root.setBackgroundResource(R.drawable.border_nonfill_graystroke_15radius)
            holder.binding.itemBookGardenNameTv.setTextColor(Color.BLACK)
        }

        // 클릭 이벤트 리스너 설정
        holder.itemView.setOnClickListener {
            // 정원을 클릭했을 때 클릭된 정원의 ID를 전달
            onGardenClicked(gardenId)
        }
    }

    fun setData(gardenTitles: List<String>, gardenIds: List<String>) {
        this.gardenTitles = gardenTitles
        this.gardenIds = gardenIds
        Log.d("Retrofit 정원 이름", "setData called with gardenTitles: $gardenTitles, gardenIds: $gardenIds")
        notifyDataSetChanged()  // 데이터가 변경되었음을 알림
    }


    override fun getItemCount(): Int {
        val count = minOf(gardenTitles.size, gardenIds.size)
        Log.d("Retrofit 정원 이름", "Item Count: $count")
        return count
    }

    // 아이템 뷰 객체들을 재활용하기 위해 날라가지 않도록 담음
    inner class ViewHolder(val binding: ItemBookGardenNameBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}