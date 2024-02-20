package com.example.wantplant.ui.main.plantAll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.remote.goal.response.GoalGetResult
import com.example.wantplant.databinding.ItemWaterWeekGoalBinding
import com.example.wantplant.ui.main.water.week.WaterWeekGoalRVAdapter
import com.example.wantplant.ui.main.water.week.WaterWeekTodoRVAdapter

class PlantAllGoalRVAdapter(private var goalList: List<GoalGetResult>) : RecyclerView.Adapter<PlantAllGoalRVAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onTodoAddClick(goalName: String, goalId: Long)
        fun onGoalDeleteClick(goalName: String, goalId: Long)
        fun onTodoClick2(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String, clickGoalId: Long)
        fun onOutlineWaterClick2(doneId: Long, doneBoolean: Boolean)
        fun onFillWaterClick2(doneId: Long, doneBoolean: Boolean)
    }

    private lateinit var mItemClickListener: ItemClickListener

    fun setGoalAddClick(itemClickListener: ItemClickListener) {
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemWaterWeekGoalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWaterWeekGoalBinding = ItemWaterWeekGoalBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 추가 버튼 클릭 시
        holder.binding.itemGoalPlusBtnIv.setOnClickListener {
            mItemClickListener.onTodoAddClick(goalList[position].goalTitle, goalList[position].goalId)
        }

        // 목표 제목 클릭 시
        holder.binding.itemGoalTitleTv.setOnClickListener {
            mItemClickListener.onGoalDeleteClick(goalList[position].goalTitle, goalList[position].goalId)
        }

        holder.binding.itemGoalTitleTv.text = goalList[position].goalTitle

        val todoManager = LinearLayoutManager(holder.binding.itemGoalTodoRv.context, LinearLayoutManager.VERTICAL, false)
        val todoAdapter = PlantAllTodoRVAdapter(goalList[position].todoList, goalList[position].goalTitle, goalList[position].goalId)
        holder.binding.itemGoalTodoRv.apply {
            adapter = todoAdapter
            layoutManager = todoManager
        }

        todoAdapter.setTodoClick(object: PlantAllTodoRVAdapter.TodoClickListener{
            override fun onTodoClick(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String, clickGoalId: Long) {
                mItemClickListener.onTodoClick2(clickTodoId, clickTodoTitle, clickTodoDate, clickTodoTime, clickTodoGoalTitle, clickGoalId)
            }

            override fun onOutlineWaterClick(doneId: Long, doneBoolean: Boolean) {
                mItemClickListener.onOutlineWaterClick2(doneId, doneBoolean)
            }

            override fun onFillWaterClick(doneId: Long, doneBoolean: Boolean) {
                mItemClickListener.onFillWaterClick2(doneId, doneBoolean)
            }

        })
    }
}