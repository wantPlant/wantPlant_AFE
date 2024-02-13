package com.example.wantplant.ui.main.water.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wantplant.data.remote.goal.response.GoalList
import com.example.wantplant.databinding.ItemWaterWeekGoalBinding
import com.example.wantplant.ui.main.plant.PlantDialog
import com.example.wantplant.ui.main.plant.PlantDialogInterface

class WaterWeekGoalRVAdapter(private var goals: List<GoalList>) : RecyclerView.Adapter<WaterWeekGoalRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWaterWeekGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemGoalTitleTv.text = goals[position].goalTitle

            val todoManager = LinearLayoutManager(binding.itemGoalTodoRv.context, LinearLayoutManager.HORIZONTAL, false)
            val todoAdapter = WaterWeekTodoRVAdapter(goals[position].todos, goals[position].goalTitle)
            binding.itemGoalTodoRv.apply {
                adapter = todoAdapter
                layoutManager = todoManager
            }

            todoAdapter.setTodoClick(object: WaterWeekTodoRVAdapter.TodoClickListener{

                override fun onTodoClick(clickTodoId: Long, clickTodoTitle: String, clickTodoDate: String, clickTodoTime: String, clickTodoGoalTitle: String) {
                    mItemClickListener.onTodoClick2(clickTodoId, clickTodoTitle, clickTodoDate, clickTodoTime, clickTodoGoalTitle)
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
    ): WaterWeekGoalRVAdapter.ViewHolder {
        val binding : ItemWaterWeekGoalBinding = ItemWaterWeekGoalBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterWeekGoalRVAdapter.ViewHolder, position: Int) {
        holder.bind(position)
        holder.binding.itemGoalPlusBtnIv.setOnClickListener {
            mItemClickListener.onTodoAddClick(goals[position].goalTitle, goals[position].goalId)
        }
    }

    override fun getItemCount(): Int {
        return goals.size
    }


}