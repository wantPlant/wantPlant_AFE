package com.example.wantplant.ui.main.book

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.R
import com.example.wantplant.databinding.ItemBookGardenNameBinding

private var selectedItemPosition: Int = RecyclerView.NO_POSITION

class BookGardenNameRVAdapter (private val bookGardenNameList: ArrayList<BookGardenName>): RecyclerView.Adapter<BookGardenNameRVAdapter.ViewHolder>() {

    init {
        // 초기에 첫 번째 아이템을 선택 상태로 설정
        if (bookGardenNameList.isNotEmpty()) {
            selectedItemPosition = 0
        }
    }

    // 리사이클러뷰에서는 클릭 이벤트가 내장되어 있지 않음 -> 클릭 리스너 역할을 하는 인터페이스 생성
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    // 어뎁터 외부의 프레그먼트에서 리스너 객체를 던져주면 됨
    private lateinit var mItemClickListener: ItemClickListener
    //private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    fun setItemClickListener(itemClickListener: ItemClickListener){
        mItemClickListener = itemClickListener
    }

    // 뷰 홀더를 생성할 때 호출
    // 아이템 뷰 객채를 만든 후 재활용하기 위해 뷰 홀더에 던져줌
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): BookGardenNameRVAdapter.ViewHolder {
        val binding = ItemBookGardenNameBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding) // 아이템 뷰 객체 던짐
    }

    // 뷰 홀더에 데이터를 바인딩해줘야 할 때마다(사용자가 화면을 위아래로 스크롤 할 때마다) 호출
    // position -> 리사이클러뷰에서의 인덱스 id
    // 받아온 뷰 홀더에 바인딩해주기 위해 리스트에서 해당 포지션에 위치하는 데이터를 ViewHolder의 bind 함수에 던져줌
    // 포지션 값을 가지고 있기 때문에 클릭 이벤트는 이곳에서 작성
    override fun onBindViewHolder(holder: BookGardenNameRVAdapter.ViewHolder, position: Int) {
        holder.bind(bookGardenNameList[position], position)
        holder.itemView.setOnClickListener{ // 어뎁터 클래스 안에서만 유효
            mItemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return bookGardenNameList.size
    }

    // 아이템 뷰 객체들을 재활용하기 위해 날라가지 않도록 담음
    // 매개변수: 아이템 뷰 객체
    // 뷰 홀더 클래스 상속 받음
    inner class ViewHolder(val binding: ItemBookGardenNameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookGardenName: BookGardenName, position: Int) {
            binding.itemBookGardenNameTv.text = bookGardenName.gardenName
            if (position == selectedItemPosition) {
                binding.itemBookGardenNameTv.setBackgroundResource(R.drawable.border_nonfill_greenstroke_15radius)
                binding.itemBookGardenNameTv.setTextColor(Color.BLACK)

            } else {
                binding.itemBookGardenNameTv.setBackgroundResource(R.drawable.border_nonfill_graystroke_15radius)
                binding.itemBookGardenNameTv.setTextColor(Color.parseColor("#616161"))
            }
        }
    }

    fun setSelectedItemPosition(position: Int) {
        // 기존에 선택된 아이템의 위치를 업데이트
        //val previousSelectedItemPosition = selectedItemPosition
        selectedItemPosition = position
        // 변경된 부분만 업데이트
        //notifyItemChanged(previousSelectedItemPosition)
        //notifyItemChanged(selectedItemPosition)
        notifyDataSetChanged()
    }
}