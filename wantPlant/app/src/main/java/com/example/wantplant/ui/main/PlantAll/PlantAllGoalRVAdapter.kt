package com.example.wantplant.ui.main.plantall

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.local.Goal
import com.example.wantplant.databinding.ItemWaterWeekGoalBinding

class PlantAllGoalRVAdapter(private var goals: List<Goal>) : RecyclerView.Adapter<PlantAllGoalRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWaterWeekGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemGoalTitleTv.text = goals[position].goalTitle // 목표 이름 표시

            val todoManager = LinearLayoutManager(binding.itemGoalTodoRv.context, LinearLayoutManager.HORIZONTAL, false) // 수평 정렬
            val todoAdapter = PlantAllTodoRVAdapter(goals[position].todoList, goals[position].goalTitle) // 할 일 어뎁터에 인자로 목표의 할 일들 및 목표 이름 전달

            binding.itemGoalTodoRv.apply {
                adapter = todoAdapter
                layoutManager = todoManager
            } // 할 일 리사이클러뷰 어뎁터 및 레이아웃 메니저 설정

            // 클릭 이벤트를 처리하기 위한 콜백 설정
            todoAdapter.setTodoClick(object: PlantAllTodoRVAdapter.TodoClickListener {

                // 할 일 클릭 시 이벤트 - 할 일 ID, 제목, 날짜, 시간, 목표 제목 전달
                override fun onTodoClick(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String) {
                    mItemClickListener.onTodoClick2(clickTodoId, clickTodoTitle, clickTodoDate, clickTodoTime, clickTodoGoalTitle)
                }

                // 완료되지 않은 할 일의 물방울 클릭 시 이벤트 - ID, 완료 여부 전달
                override fun onOutlineWaterClick(doneId: Long, doneBoolean: Boolean) {
                    mItemClickListener.onOutlineWaterClick2(doneId, doneBoolean)
                }

                // 완료한 할 일의 물방울 클릭 시 이벤트 - ID, 완료 여부 전달
                override fun onFillWaterClick(doneId: Long, doneBoolean: Boolean) {
                    mItemClickListener.onFillWaterClick2(doneId, doneBoolean)
                }
            })
        }
    }

    interface ItemClickListener {
        fun onTodoAddClick(goalName: String, goalId: Long)
        fun onTodoClick2(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String)
        fun onOutlineWaterClick2(doneId: Long, doneBoolean: Boolean)
        fun onFillWaterClick2(doneId: Long, doneBoolean: Boolean)
    }

    private lateinit var mItemClickListener: ItemClickListener

    fun setGoalAddClick(itemClickListener: ItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding : ItemWaterWeekGoalBinding = ItemWaterWeekGoalBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.binding.itemGoalPlusBtnIv.setOnClickListener {
            mItemClickListener.onTodoAddClick(goals[position].goalTitle, goals[position].goalId)
        } // 목표 옆 플러스 아이콘 클릭 시 목표 추가
    }

    override fun getItemCount(): Int {
        return goals.size
    }
}